package br.senai.sp.jandira.ayancare_frontmobile.screens.finalizarCadastro.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import br.senai.sp.jandira.ayancare_frontmobile.MainActivity
import br.senai.sp.jandira.ayancare_frontmobile.R
import br.senai.sp.jandira.ayancare_frontmobile.components.DateTextField
import br.senai.sp.jandira.ayancare_frontmobile.components.DefaultButton
import br.senai.sp.jandira.ayancare_frontmobile.components.TextFieldNumber
import br.senai.sp.jandira.ayancare_frontmobile.components.Wave
import br.senai.sp.jandira.ayancare_frontmobile.retrofit.user.repository.CadastroRepository
import br.senai.sp.jandira.ayancare_frontmobile.screens.Storage
import br.senai.sp.jandira.ayancare_frontmobile.screens.cadastro.components.ProgressBar
import br.senai.sp.jandira.ayancare_frontmobile.screens.finalizarCadastro.components.DropdownGender
import br.senai.sp.jandira.ayancare_frontmobile.sqlite.funcaoQueChamaSqlLite.saveLogin
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun FinalizarCadastroScreen(
    navController: NavController,
    lifecycleScope: LifecycleCoroutineScope,
    localStorage: Storage
) {

    val context = LocalContext.current

    var cpfState by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedDrop by remember { mutableStateOf("") }

    //Obter foto da galeria de imagens
    //variavel que vai guardar a uri
    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        photoUri = it
    }

    var painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(photoUri).build()
    )

    val id = localStorage.lerValor(context, "id_paciente")
    val token = localStorage.lerValor(context, "token_paciente")
    val nome = localStorage.lerValor(context, "nome_paciente")
    val email = localStorage.lerValor(context, "email_paciente")
    val senha = localStorage.lerValor(context, "senha_paciente")

    val id_paciente = id?.toInt()

    fun finalizarCadastro(
        token: String,
        id: Int,
        nome: String,
        data_nascimento: String,
        email: String,
        senha: String,
        cpf: String,
        id_endereco_paciente: Int,
        id_genero: Int
    ) {
        val userRepository = CadastroRepository()
        lifecycleScope.launch {

            val response = userRepository.updateUser(
                token,
                id,
                nome,
                data_nascimento,
                email,
                senha,
                cpf,
                id_endereco_paciente,
                id_genero
            )





            Log.e("response", "finalizarCadastro: $response", )

            if (response.isSuccessful) {

                saveLogin(
                    context = context,
                    id = id.toLong(),
                    nome = nome,
                    token = token!!,
                    email = email,
                    dataNascimento = selectedDate,
                    genero = selectedDrop,
                    tipo = "Paciente"
                )

                Log.d(MainActivity::class.java.simpleName, "Registro bem-sucedido")

                navController.navigate("endereco_paciente_screen")

            } else {

                val errorBody = response.errorBody()?.string()
                Log.e(MainActivity::class.java.simpleName, "Erro durante o registro: $errorBody")
                Toast.makeText(context, "Erro durante o registro", Toast.LENGTH_SHORT).show()

            }
        }

    }

    Surface(
        color = Color(248, 240, 236)
    ) {
        Wave()
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 40.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Finalizar Cadastro",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                )
                Text(
                    text = "Preencha os dados restantes para finalizar seu cadastro.",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9E8BC1),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center),
                            shape = CircleShape,
                            border = BorderStroke(
                                width = 2.dp,
                                Brush.horizontalGradient(
                                    listOf(
                                        colorResource(id = R.color.purple_200),
                                        Color.White
                                    )
                                )
                            )
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "imagem do usuário",
                                //colorFilter = ColorFilter.tint(colorResource(id = R.color.pink_login)),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Image(
                            painter = painterResource(
                                id = R.drawable.baseline_camera_alt_24
                            ),
                            contentDescription = "",
                            modifier = Modifier
                                .align(alignment = Alignment.BottomEnd)
                                .offset(x = 3.dp, y = 3.dp)
                                .size(30.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                },
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextFieldNumber(
                        valor = cpfState,
                        label = "CPF",
                        onValueChange = { cpfState = it }
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    DateTextField(
                        context,
                        selectedDate,
                        onDateChange = { selectedDate = it }
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    DropdownGender(
                        context = context,
                        gender = selectedDrop,
                        onValueChange = { selectedDrop = it }
                    )

                    // Agora você pode acessar 'dateTyped' que contém o valor digitado
                    Text("genero digitada na tela principal: ${cpfState + selectedDate + selectedDrop}")
                }

            }

            Column(
                modifier = Modifier.width(190.dp)
            ) {
                DefaultButton(
                    text = "Finalizar",
                    onClick = {
                        if (id_paciente != null) {
                            finalizarCadastro(
                                token = token.toString(),
                                id = id_paciente.toInt(),
                                nome = nome.toString(),
                                data_nascimento = selectedDate.toAmericanDateFormat(),
                                email.toString(),
                                senha.toString(),
                                cpf = cpfState,
                                id_endereco_paciente = 1,
                                id_genero = 1 // lohannes precisa mudar isso para o nome do genero
                            )
                        }
                    }
                )
            }
            ProgressBar(text = "2 / 4", valor = 165)

        }
    }
}

fun String.toAmericanDateFormat(
    pattern: String = "yyyy-MM-dd"
): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(
        pattern, Locale("pt-br")
    ).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
    return formatter.format(date)
}
