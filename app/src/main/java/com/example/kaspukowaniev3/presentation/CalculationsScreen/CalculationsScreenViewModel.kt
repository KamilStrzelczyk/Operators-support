package com.example.kaspukowaniev3.presentation.CalculationsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kaspukowaniev3.domain.repository.RecipeRepository
import com.example.kaspukowaniev3.domain.usecase.CalculationsScreenUseCase.*
import com.example.kaspukowaniev3.presentation.Utils
import com.example.kaspukowaniev3.presentation.Utils.Companion.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculationsScreenViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val calculateAmountOfFillCapsules: CalculateAmountOfFillCapsulesUseCase,
    private val calculateAmountOfWastePowder: CalculateAmountOfWastePowderUseCase,
    private val calculateAmountOfRemainingCapsules: CalculateAmountOfRemainingCapsulesUseCase,
    private val calculateOfEfficiency: CalculateOfEfficiencyUseCase,
    private val calculateWeightOfFinishedProducts: CalculateWeightOfFinishedProductsUseCase,
) : ViewModel() {
    val state = mutableStateOf(ViewModelState())

    init {
        val amountOfCapsules = recipeRepository.getAmount()
        val boxWeight = recipeRepository.getBoxWeight()
        val weightOfPowder = recipeRepository.weightOfPowder()
        state.value = state.value.copy(
            amountOfCapsules = amountOfCapsules,
            boxWeight = boxWeight,
            weightOfPowder = weightOfPowder,
        )
    }

    fun onFullBoxesChanged(fullBoxes: String) {
        val amountOfFillCapsules = calculateAmountOfFillCapsules(
            fullBoxes = fullBoxes,
            boxWeight = state.value.boxWeight,
            restOfBoxes = state.value.restOfBoxes,
            capsulesGross = state.value.capsulesGross,
        )
        updateState(state.value.copy(
            amountOfFillCapsules = amountOfFillCapsules,
            fullBoxes = fullBoxes,
        ))
        val amountofweight1 = calculateWeightOfFinishedProducts(
            fullBoxes = fullBoxes,
            boxWeight = state.value.boxWeight,
            restOfBoxes = state.value.restOfBoxes,
        )
        updateState(state.value.copy(
            weightOfFinishedProducts = amountofweight1,
        ))
        val efficiency = calculateOfEfficiency(
            weightOfFinishedProducts = state.value.weightOfFinishedProducts,
            weightOfPowder = state.value.weightOfPowder,
        )
        updateState(state.value.copy(
            efficiency = efficiency
        ))

    }

    fun onRestBoxesChanged(restOfBoxes: String) {
        val amountOfFillCapsules = calculateAmountOfFillCapsules(
            restOfBoxes = restOfBoxes,
            fullBoxes = state.value.fullBoxes,
            boxWeight = state.value.boxWeight,
            capsulesGross = state.value.capsulesGross,
        )
        updateState(state.value.copy(
            amountOfFillCapsules = amountOfFillCapsules,
            restOfBoxes = restOfBoxes,
        ))
        val amountOfWeight = calculateWeightOfFinishedProducts(
            restOfBoxes = restOfBoxes,
            boxWeight = state.value.boxWeight,
            fullBoxes = state.value.fullBoxes,
        )
        updateState(state.value.copy(
            weightOfFinishedProducts = amountOfWeight,
        ))

        val efficiency = calculateOfEfficiency(
            weightOfFinishedProducts = state.value.weightOfFinishedProducts,
            weightOfPowder = state.value.weightOfPowder,
        )
        updateState(state.value.copy(
            efficiency = efficiency
        ))
    }

    fun onCapsulesGrossChanged(capsulesGross: String) {
        val newValue = capsulesGross.replace(Utils.COMMA, Utils.DOT)
        val amountOfFillCapsules = calculateAmountOfFillCapsules(
            capsulesGross = newValue,
            fullBoxes = state.value.fullBoxes,
            restOfBoxes = state.value.restOfBoxes,
            boxWeight = state.value.boxWeight,
        )
        updateState(state.value.copy(
            amountOfFillCapsules = amountOfFillCapsules,
            capsulesGross = capsulesGross,
        ))
    }

    fun onCapsulesNettChanged(capsulesNett: String) {
        val wasteOfPowder = calculateAmountOfWastePowder(
            capsulesNett = capsulesNett,
            amountOfFillCapsules = state.value.amountOfFillCapsules,
            weightOfPowder = state.value.weightOfPowder,
        )
        updateState(state.value.copy(
            wasteOfPowder = wasteOfPowder,
            capsulesNett = capsulesNett,
        ))
    }

    fun onWrongCapsulesChanged(wrongCapsules: String) {
        val restOfCapsules = calculateAmountOfRemainingCapsules(
            wrongCapsules = wrongCapsules,
            amountOfCapsules = state.value.amountOfCapsules,
            amountOfFillCapsules = state.value.amountOfFillCapsules,
        )
        updateState(state.value.copy(
            restOfCapsules = restOfCapsules,
            wrongCapsules = wrongCapsules,
        ))
    }

    private fun updateState(state: ViewModelState) {
        this.state.value = state
    }

    data class ViewModelState constructor(
        val fullBoxes: String = EMPTY_STRING,
        val fullBoxesHint: String = "Ilość pełnych pojemników",
        val restOfBoxes: String = EMPTY_STRING,
        val restOfBoxesHint: String = "Ilość z niepełnych pojemników",
        val capsulesGross: String = EMPTY_STRING,
        val capsulesGrossHint: String = "Brutto",
        val capsulesNett: String = EMPTY_STRING,
        val capsulesNettHint: String = "Netto",
        val wasteOfPowderHint: String = "Odpad Proszku",
        val wasteOfPowder: String = EMPTY_STRING,
        val amountOfFillCapsules: String = EMPTY_STRING,
        val amountOfFillCapsulesHint: String = "Ilość gotowych kaspułek",
        val efficiencyText: String = "Wydajność",
        val efficiency: String = EMPTY_STRING,
        val restOfCapsulesHint: String = "Pozostała ilość kapsułek",
        val restOfCapsules: String = EMPTY_STRING,
        val amountOfCapsules: String = EMPTY_STRING,
        val boxWeight: String = EMPTY_STRING,
        val wrongCapsulesHint: String = "Odpad",
        val wrongCapsules: String = EMPTY_STRING,
        val weightOfPowder: String = EMPTY_STRING,
        val weightOfFinishedProductsText: String = "Waga gotowego wyrobu",
        val weightOfFinishedProducts: String = EMPTY_STRING,
        val buttonText: String = "Zapisz dane serii",

        // LABELS
        val capsuleWeightsLabel: String = "Wagi kapsułek",
        val processWasteLabel: String = "Odpad z procesu",
        val resultLabel: String = "Wynik",
        val topAppBarLabel: String = "Rozliczenie kapsułkowania",

        // Unit
        val kg: String = "kg",
        val mg: String = "mg",
        val pc: String = "szt.",
    )
}