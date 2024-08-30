package com.peakDevCol.gympartner.domain

import com.peakDevCol.gympartner.data.repository.HomeRepository
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * The UseCase are the actions that the user can do it.
 * In this case, for example, the user makes a login.
 */
class GetListBodyPartUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    /**
     * invoke it's a type of function that you can use only call the instance of class
     * for example, loginUseCase(juan@prueba.com, 123456)
     **/
    operator fun invoke(): Flow<BodyPartModel> {
        return homeRepository.getLocalBodyPart()
    }

}
