package br.senac.lojaretrofit.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.senac.lojaretrofit.databinding.ActivityListaProdutosBinding
import br.senac.lojaretrofit.databinding.CardItemBinding
import br.senac.lojaretrofit.model.Produto
import br.senac.lojaretrofit.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import java.net.CacheResponse
import javax.security.auth.callback.Callback

class ListaProdutosActivity : AppCompatActivity() {
    lateinit var binding: ActivityListaProdutosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listarProdutos()
    }

    fun listarProdutos() {
        val callback = object : retrofit2.Callback<List<Produto>>{
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful){
                    val ListaProdutos = response.body()
                    atualizarTela(ListaProdutos)
                }

                binding.progressBar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Snackbar.make(binding.container,
                "Não",
                    Snackbar.LENGTH_LONG)
                    .show()
                binding.progressBar.visibility = View.INVISIBLE
            }

        }

        API.produto.listar().enqueue(callback)
        binding.progressBar.visibility = View.VISIBLE
    }

    fun atualizarTela (listaProdutos: List<Produto>?) {
        binding.container.removeAllViews()

        listaProdutos?.forEach {
            val cardBinding = CardItemBinding.inflate(layoutInflater)

            cardBinding.textNome.text = it.nomeProduto
            cardBinding.textNome.text = it.precProduto.toString()

            Picasso
                .get()
                .load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/${it.idProduto}")
                .into(cardBinding.image)


            binding.container.addView(cardBinding.root)

        }
    }

}