package com.peakDevCol.gympartner.data.local

import com.peakDevCol.gympartner.data.local.entities.BodyPartEntity
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


internal fun BodyPartModel.toEntity() = BodyPartEntity(bodyParts = bodyParts)

internal fun BodyPartEntity.toDomain() = BodyPartModel(bodyParts = bodyParts)

internal fun <T, R> Flow<T>.transform(transformer: (T) -> R): Flow<R> {
    return this.map { item -> transformer(item) }
}

//internal fun List<OrderEntity>.toDomainList() = this.map { orderEntity -> orderEntity.toDomain() }
