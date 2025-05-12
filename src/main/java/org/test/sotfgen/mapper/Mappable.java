package org.test.sotfgen.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface Mappable<E, D> {

    E toEntity(D dto);

    List<E> toEntity(List<D> dtos);

    D toDto(E entity);

    List<D> toDto(List<E> entities);

    void updateEntity(D dto, @MappingTarget E entity);
}
