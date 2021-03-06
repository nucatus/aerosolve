package com.airbnb.aerosolve.core.transforms;

import com.airbnb.aerosolve.core.FeatureVector;
import com.typesafe.config.Config;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 * Created by hector_yee on 8/25/14.
 * Takes the cross product of stringFeatures named in field1 and field2
 * and places it in a stringFeature with family name specified in output.
 */
public class CrossTransform extends Transform {
  private String fieldName1;
  private String fieldName2;
  private String outputName;

  @Override
  public void configure(Config config, String key) {
    fieldName1 = config.getString(key + ".field1");
    fieldName2 = config.getString(key + ".field2");
    outputName = config.getString(key + ".output");
  }

  @Override
  public void doTransform(FeatureVector featureVector) {
    Map<String, Set<String>> stringFeatures = featureVector.getStringFeatures();
    if (stringFeatures == null) return;

    Set<String> set1 = stringFeatures.get(fieldName1);
    if (set1 == null || set1.isEmpty()) return;
    Set<String> set2 = stringFeatures.get(fieldName2);
    if (set2 == null || set2.isEmpty()) return;

    Set<String> output = stringFeatures.get(outputName);
    if (output == null) {
      output = new HashSet<>();
      stringFeatures.put(outputName, output);
    }
    cross(set1, set2, output);
  }

  public static void cross(Set<String> set1, Set<String> set2, Set<String> output) {
    for (String s1 : set1) {
      String prefix = s1 + '^';
      for (String s2 : set2) {
        output.add(prefix + s2);
      }
    }
  }
 }
