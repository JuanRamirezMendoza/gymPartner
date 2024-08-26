package com.peakDevCol.gympartner.domain

import com.peakDevCol.gympartner.data.repository.HomeRepository
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import javax.inject.Inject


/**
 * The UseCase are the actions that the user can do it.
 * In this case, for example, the user makes a login.
 */
class SaveListBodyPartUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    /**
     * invoke it's a type of function that you can use only call the instance of class
     * for example, loginUseCase(juan@prueba.com, 123456)
     **/
    suspend operator fun invoke(bodyPartModel: BodyPartModel) {
        homeRepository.saveBodyPart(bodyPartModel)
    }

}
