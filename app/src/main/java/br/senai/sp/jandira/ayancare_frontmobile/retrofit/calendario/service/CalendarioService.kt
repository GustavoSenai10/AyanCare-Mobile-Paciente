package br.senai.sp.jandira.ayancare_frontmobile.retrofit.calendario.service

import br.senai.sp.jandira.ayancare_frontmobile.retrofit.calendario.CalendarioResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarioService {

    @GET("/v1/ayan/calendario")
    fun getCalendarioByIDPacienteDia_DiaSemana(@Query("idPaciente") idPaciente: Int, @Query("dia") dia: String,  @Query("diaSemana") diaSemana: String): retrofit2.Call<CalendarioResponse>

}