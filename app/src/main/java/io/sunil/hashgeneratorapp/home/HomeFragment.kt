package io.sunil.hashgeneratorapp.home

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.sunil.hashgeneratorapp.R
import io.sunil.hashgeneratorapp.databinding.FragmentHomeBinding
import io.sunil.hashgeneratorapp.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()

    private val homeBinding get() = _fragmentHomeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)


        setHasOptionsMenu(true)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
//        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_hash_algo_item, hashAlgorithms)
//        homeBinding.autoCompleteTextView.setAdapter(arrayAdapter)

        homeBinding.generateButton.setOnClickListener {
            onGenerateClick()
        }
    }

    private fun onGenerateClick(){

        if(homeBinding.plainEditText.text.isEmpty()){

            showSnackBar("Field Empty.")

        }
        else{

            lifecycleScope.launch {
                applyAnimations()
                navigateToSuccess(getHashData())
            }
        }

    }

    private suspend fun applyAnimations(){
        homeBinding.generateButton.isClickable = false
        homeBinding.titleTextView.animate().alpha(0f).duration = 400L
        homeBinding.generateButton.animate().alpha(0f).duration = 400L
        homeBinding.textInputLayout.animate().alpha(0f).translationXBy(1200f).duration = 400L
        homeBinding.plainEditText.animate().alpha(0f).translationXBy(-1200f).duration = 400L

        delay(300)

        homeBinding.successBackground.animate().alpha(1f).duration = 600L
        homeBinding.successBackground.animate().rotationBy(720f).duration = 600L
        homeBinding.successBackground.animate().scaleXBy(900f).duration = 800L
        homeBinding.successBackground.animate().scaleYBy(900f).duration = 800L

        delay(500)

        homeBinding.successImageView.animate().alpha(1f).duration = 1000L

        delay(1500L)

    }

    private fun getHashData():String{
        val algorithm = homeBinding.autoCompleteTextView.text.toString()
        val plainText = homeBinding.plainEditText.text.toString()
        return  homeViewModel.getHash(plainText, algorithm)
    }

    private fun navigateToSuccess(hash: String){
        val directions = HomeFragmentDirections.actionHomeFragmentToSuccessFragment(hash)
        findNavController().navigate(directions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.clear_menu){
            homeBinding.plainEditText.text.clear()

            showSnackBar("Cleared..")
            return true
        }
        return true

    }

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_hash_algo_item, hashAlgorithms)
        homeBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }
    override fun onDestroyView() {
        super.onDestroyView()

        _fragmentHomeBinding = null
    }

    private fun showSnackBar(message:String){

        val snackBar = Snackbar.make(
            homeBinding.rootLayout,
            message,
            Snackbar.LENGTH_SHORT
        )

        snackBar.setAction("OK"){

        }
        snackBar.show()

    }
}