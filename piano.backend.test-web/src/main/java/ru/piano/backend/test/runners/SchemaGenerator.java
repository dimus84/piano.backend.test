
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
