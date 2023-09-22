package com.example.upload_firebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.upload_firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    // declaracao dos atributos
    // ACTIVITYMAINBINDIND -> manipulação dos elementos gráficos do material design
    private lateinit var binding : ActivityMainBinding
    //permite a manipulacao do cloud Storage (armazena Arquivos)
    private lateinit var storageRef : StorageReference
    // perminite a manipulacao de dados NOSQL
    private lateinit var firebaseFireStorage:FirebaseStorage
    //URI - Permite a manipulação de arquivos atravédo seu Endereçamento
    private var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    //antes para o carregamento usavamos
        setContentView(binding.root)//setContentView(R.layout.activity_main)
        initVars()
        registerclickEvents()
    }

    //Função de inicialização dos recursos do fireBae
    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("images")
        firebaseFireStorage = FirebaseStorage.getInstance()

    }
    //Função para o lançdor de  recuperação de imagens da galeria
    private val resultLaucher = registerForActivityResult(//objeto estatico que me traz o result de todos os register, atividade de telas
        ActivityResultContracts.GetContent() // classe estatica que pega os conteudos do GEtContent- . componenete visual que quero
    ){
        imageUri = it
        binding.imageView.setImageURI(it)
    }
//função de tratamentos de clcicks
    private fun registerclickEvents() {
        //trata o evento de click do componente IMAGEVIEW
        binding.imageView.setOnClickListener{
            resultLaucher.launch("image/*")
        }

    //trata o evento de click do botao upload

    binding.uploadBtn.setOnClickListener{
        uploadImage()
    }
}
//funçao sde upload para o fierbase
    private fun uploadImage() {
    binding.progressBar.visibility = View.VISIBLE
        // define o nome único para a imagem como uso de um valor  TIMESTEMPE
        storageRef = storageRef.child(System.currentTimeMillis().toString()) // converte o tempo em string
        // execulta o processo de UPLOAD da imagem
        imageUri?.let{
            storageRef.putFile(it).addOnCompleteListener {
                task ->
                if (task.isComplete){
                    Toast.makeText(this,"UPLOAD comcluido",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Erro ao realizarUpload ",Toast.LENGTH_LONG)
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}