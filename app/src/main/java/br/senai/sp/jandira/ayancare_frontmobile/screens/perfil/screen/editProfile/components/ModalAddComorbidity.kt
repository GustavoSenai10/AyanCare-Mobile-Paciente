package br.senai.sp.jandira.ayancare_frontmobile.screens.perfil.screen.editProfile.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import br.senai.sp.jandira.ayancare_frontmobile.MainActivity
import br.senai.sp.jandira.ayancare_frontmobile.R
import br.senai.sp.jandira.ayancare_frontmobile.components.CustomTextFieldValidate
import br.senai.sp.jandira.ayancare_frontmobile.components.DefaultButton
import br.senai.sp.jandira.ayancare_frontmobile.components.DefaultTextField
import br.senai.sp.jandira.ayancare_frontmobile.retrofit.user.repository.ComorbidityRepository
import br.senai.sp.jandira.ayancare_frontmobile.sqlite.repository.PacienteRepository
import kotlinx.coroutines.launch

@Composable
fun ModalAddComorbidity(
    isDialogVisibleComorbidity: Boolean,
    navController: NavController,
    lifecycleScope: LifecycleCoroutineScope,
    nav: String
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val array = PacienteRepository(context = context).findUsers()

    val paciente = array[0]
    var id = paciente.id.toLong()

    var nomeState by remember {
        mutableStateOf("")
    }

    var validateName by rememberSaveable {
        mutableStateOf(true)
    }

    val validateNameError = "Nome está em branco"

    fun validateData(
        name: String,
    ): Boolean {

        validateName = name.isNotBlank()

        return validateName
    }


    fun chronicDiseases(
        nome: String,
        id_paciente: Int
    ) {
        if (validateData(nome)){

            val comorbidityRepository = ComorbidityRepository()
            lifecycleScope.launch {

                val response = comorbidityRepository.registerComorbidity(
                    nome,
                    id_paciente
                )

                if (response.isSuccessful) {
                    Log.e(MainActivity::class.java.simpleName, "responsible bem-sucedido")
                    Log.e("responsible", "responsible: ${response.body()}")
                    val checagem = response.body()?.get("status")

                    Log.e("responsible", "responsible: ${checagem}")

                    if (checagem.toString() == "404") {
                        Toast.makeText(context, "algo está invalido", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Sucesso!!", Toast.LENGTH_SHORT).show()
                        navController.navigate("$nav")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()

                    Log.e(MainActivity::class.java.simpleName, "Erro durante o responsible: $errorBody")
                    Toast.makeText(context, "algo está invalido", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(context, "Por favor, reolhe suas caixas de texto", Toast.LENGTH_SHORT).show()
        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Dialog(
            onDismissRequest = {
                isDialogVisibleComorbidity
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Adicionar \n Comorbidade",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

//                    DefaultTextField(
//                        valor = nomeState,
//                        label = "Nome",
//                        onValueChange = { nomeState = it },
//                        aoMudar = {}
//                    )

                    CustomTextFieldValidate(
                        value = nomeState,
                        onValueChange = { nomeState = it },
                        label = "Nome:",
                        showError = !validateName,
                        errorMessage = validateNameError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        unfocusedBorderColor = Color(0xFF64748B),
                        focusedBorderColor = Color(0xFF6650A4),
                        textColor = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    DefaultButton(
                        onClick = {
                            chronicDiseases(
                                nome = nomeState,
                                id_paciente = id.toInt()
                            )
                        },
                        text = "Salvar"
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Cancelar",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF35225F),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("$nav")
                            }
                    )
                }
            }
        }
    }
}
