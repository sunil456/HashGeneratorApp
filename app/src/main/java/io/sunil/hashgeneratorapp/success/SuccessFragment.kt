package io.sunil.hashgeneratorapp.success

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import io.sunil.hashgeneratorapp.databinding.FragmentSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Condition


class SuccessFragment : Fragment() {

    private var _fragmentSuccessBinding: FragmentSuccessBinding? = null

    private val successBinding get() = _fragmentSuccessBinding!!

    private val args : SuccessFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _fragmentSuccessBinding = FragmentSuccessBinding.inflate(inflater, container, false)

        return successBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        successBinding.apply {
            hashTextView.text = args.hashData

            copyButton.setOnClickListener {
                onCopyClicked()
            }
        }

    }

    private fun onCopyClicked() {

        lifecycleScope.launch {
            copyToClipboard(args.hashData)
            applyAnimation()
        }

    }

    private fun copyToClipboard(hashData: String) {
        val clipBoardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encrypted Text", hashData)

        clipBoardManager.setPrimaryClip(clipData)
    }

    private suspend fun applyAnimation(){
        successBinding.include.messageBackgroundView.animate().translationY(80f).duration = 200L
        successBinding.include.messageTextView.animate().translationY(80f).duration = 200L

        delay(2000L)

        successBinding.include.messageBackgroundView.animate().translationY(-80f).duration = 500L
        successBinding.include.messageTextView.animate().translationY(-80f).duration = 500L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentSuccessBinding = null
    }


}