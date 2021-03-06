/*
 * Copyright 2014 Rodrigo Agerri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package eus.ixa.ixa.pipe.ml.features;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.featuregen.ArtifactToSerializerMapper;
import opennlp.tools.util.featuregen.CustomFeatureGenerator;
import opennlp.tools.util.featuregen.FeatureGeneratorResourceProvider;
import opennlp.tools.util.model.ArtifactSerializer;
import eus.ixa.ixa.pipe.ml.resources.WordCluster;
import eus.ixa.ixa.pipe.ml.utils.Flags;

public class Word2VecClusterFeatureGenerator extends CustomFeatureGenerator implements ArtifactToSerializerMapper {
  
  private WordCluster word2vecCluster;
  private static String unknownClass = "O";
  private Map<String, String> attributes;
  
  
  public Word2VecClusterFeatureGenerator() {
  }
  
  public void createFeatures(List<String> features, String[] tokens, int index,
      String[] previousOutcomes) {
    
    String wordClass = getWordClass(tokens[index].toLowerCase());
    features.add(attributes.get("dict") + "=" + wordClass);
    if (Flags.DEBUG) {
      System.err.println("-> " + tokens[index].toLowerCase() + ": " + attributes.get("dict") + "=" + wordClass);
    }
  }
  
  private String getWordClass(String token) {
    String wordClass = word2vecCluster.lookupToken(token);
    if (wordClass == null) {
      wordClass = unknownClass;
    }
    return wordClass;
  }

  @Override
  public void updateAdaptiveData(String[] tokens, String[] outcomes) {
    
  }

  @Override
  public void clearAdaptiveData() {
    
  }

  @Override
  public void init(Map<String, String> properties,
      FeatureGeneratorResourceProvider resourceProvider)
      throws InvalidFormatException {
    Object dictResource = resourceProvider.getResource(properties.get("dict"));
    if (!(dictResource instanceof WordCluster)) {
      throw new InvalidFormatException("Not a ClusterLexicon resource for key: " + properties.get("dict"));
    }
    this.word2vecCluster = (WordCluster) dictResource;
    this.attributes = properties;
  }
  
  @Override
  public Map<String, ArtifactSerializer<?>> getArtifactSerializerMapping() {
    Map<String, ArtifactSerializer<?>> mapping = new HashMap<>();
    mapping.put("word2vecserializer", new WordCluster.WordClusterSerializer());
    return Collections.unmodifiableMap(mapping);
  }
}
