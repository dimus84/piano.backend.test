
package ru.piano.backend.test.mappers;

import java.util.List;
import org.mapstruct.MappingTarget;

/**
 * 
 * @param <E> entity class
 * @param <D> dto class
 */
public interface GenericMapper<E,D> {

    D createFromEntity(E entity);

    E createFromDto(D dto);

    void updateEntity(@MappingTarget E entity, D dto);

    List<D> createFromEntities(List<E> entities);

    List<E> createFromDtos(List<D> dtos);

}
