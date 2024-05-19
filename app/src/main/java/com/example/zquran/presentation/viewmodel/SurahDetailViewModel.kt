package com.example.zquran.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zquran.domain.SurahDetail
import com.example.zquran.domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SurahDetailViewModel(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _surahDetailData: MutableLiveData<SurahDetail> = MutableLiveData()
    val surahDetailData: LiveData<SurahDetail> = _surahDetailData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

    fun fetchSurahDataDetail(noSurah: Int) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mainRepository.fetchSurahDetail(noSurah)
            }.onFailure { exception ->
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _error.value = exception.message
                }
            }.onSuccess { surah ->
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _surahDetailData.value = surah
                }
            }
        }
    }
}