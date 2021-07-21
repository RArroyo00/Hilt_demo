package com.app.bikercopilot.mappers

import com.amplifyframework.datastore.generated.model.Biker
import com.app.bikercopilot.ui.models.BikerUIModel

fun Biker.transformToBikerUiModel(): BikerUIModel = BikerUIModel(
    id = id,
    name = name,
    profilePicture = profilePicture
)