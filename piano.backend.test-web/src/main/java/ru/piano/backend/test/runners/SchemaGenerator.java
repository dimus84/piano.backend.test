/*
 * Copyright 2017 slavb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.piano.backend.test.runners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 *
 * @author slavb
 */
public class SchemaGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, JAXBException {
        Map<String, Object> contextProperties = new HashMap();
        contextProperties.put("eclipselink.beanvalidation.facets", true);
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(SchemaGenerator.class.getClassLoader());
        List<Class> classes = new ArrayList<>();
        for (Resource resource : resolver.getResources("classpath:/ru/piano/backend/test/jpa/model/*.class")) {
            ClassMetadata metadata = factory.getMetadataReader(resource).getClassMetadata();
            classes.add(ClassUtils.forName(metadata.getClassName(), ClassUtils.getDefaultClassLoader()));
        }
        
        JAXBContext jc = JAXBContext.newInstance(classes.stream().toArray(Class[]::new),contextProperties);

        jc.generateSchema(new SchemaOutputResolver() {

            @Override
            public Result createOutput(String namespaceURI, String suggestedFileName)
                    throws IOException {
                StreamResult result = new StreamResult(System.out);
                result.setSystemId(suggestedFileName);
                return result;
            }

        });
    }

}
