package com.example.letterboxf.model.custom

import com.example.letterboxf.model.apiModels.movies.ProductionCompany
import com.example.letterboxf.model.apiModels.movies.ProductionCountry
import com.example.letterboxf.model.apiModels.movies.SpokenLanguage

data class MovieDetails(val budget : String, val studios : ProductionCompany,val countries : ProductionCountry,val languages : SpokenLanguage)
