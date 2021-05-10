package com.ibm.health.vaccination.app.vaccinee.common

import com.ibm.health.vaccination.app.vaccinee.storage.CertRepository

internal class ToggleFavoriteUseCase(private val certRepository: CertRepository) {

    suspend fun toggleFavorite(certId: String) {
        certRepository.certs.update {
            it.toggleFavorite(certId)
        }
    }
}