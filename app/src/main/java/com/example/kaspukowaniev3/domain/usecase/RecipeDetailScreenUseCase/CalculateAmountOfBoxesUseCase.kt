package com.example.kaspukowaniev3.domain.usecase.RecipeDetailScreenUseCase

import java.math.RoundingMode
import javax.inject.Inject

class CalculateAmountOfBoxesUseCase @Inject constructor() {

    operator fun invoke(
        boxWeight: String,
        weightOfPowder: String,
        doseWeight: Double,
        capsulesGross: Double,
    ): String {
        return if (isDataCorrect(boxWeight, weightOfPowder)) {
            val amountOfFullCapsules = (weightOfPowder.toInt() / doseWeight) * capsulesGross
            (amountOfFullCapsules / boxWeight.toInt())
                .toBigDecimal()
                .setScale(0, RoundingMode.UP)
                .toString()
        } else {
            ""
        }


    }

    private fun isDataCorrect(
        boxWeight: String,
        weightOfPowder: String,
    ) = weightOfPowder.isNotBlank() && boxWeight.isNotBlank() && weightOfPowder.toInt() != 0 && boxWeight.toInt() != 0


}