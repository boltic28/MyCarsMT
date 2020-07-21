package com.example.mycarsmt.domain

import com.example.mycarsmt.domain.Car
import java.io.Serializable

class DiagnosticElement(val car: Car, val list: List<String>) : Serializable