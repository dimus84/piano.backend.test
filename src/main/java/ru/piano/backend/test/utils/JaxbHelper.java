
package ru.piano.backend.test.utils;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JaxbHelper {

    @Autowired
    ContextResolver<JAXBContext> jaxbContextResolver;

    /**
     * unmarshalls object instance
     *
     * @param <T>
     * @param source example from String: new StreamSource(new
     * java.io.StringReader(string)), from InputStream: new StreamSource(is)
     * @param type
     * @param mediaType
     * @return T
     */
    public <T> T unmarshal(Source source, Class<T> type, String mediaType) {

        JAXBContext jaxbContext = jaxbContextResolver.getContext(type);
        try {
            T result;
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, mediaType);
            result = (T) unmarshaller.unmarshal(source, type);

            return result;

        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param <T>
     * @param data
     * @param type
     * @param mediaType
     * @return T
     */
    public <T> T unmarshal(String data, Class<T> type, String mediaType) {

        JAXBContext jaxbContext = jaxbContextResolver.getContext(type);
        return unmarshal(jaxbContext, data, type, mediaType);
    }

    public static <T> T unmarshal(JAXBContext jaxbContext, String data, Class<T> type, String mediaType) {
        return unmarshal(jaxbContext, new StreamSource(new java.io.StringReader(data)), type, mediaType);
    }
    public static <T> T unmarshal(JAXBContext jaxbContext, Source source, Class<T> type, String mediaType) {

        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, mediaType);
            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
            Object unmarshal;
            if (type != null) {
                unmarshal = unmarshaller.unmarshal(source, type);
            } else {
                unmarshal = unmarshaller.unmarshal(source);
            }
            if (unmarshal instanceof JAXBElement<?>) {
                unmarshal = ((JAXBElement<?>) unmarshal).getValue();
            }

            return (T) unmarshal;

        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

}
