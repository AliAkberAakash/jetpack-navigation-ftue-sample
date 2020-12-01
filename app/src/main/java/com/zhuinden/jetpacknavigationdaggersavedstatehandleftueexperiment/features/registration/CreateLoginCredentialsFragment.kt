/*
 * Copyright 2020 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.features.registration

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.R
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.application.AuthenticationManager
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.core.navigation.NavigationDispatcher
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.databinding.CreateLoginCredentialsFragmentBinding
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.accessor
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.navGraphSavedStateViewModels
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onClick
import com.zhuinden.jetpacknavigationdaggersavedstatehandleftueexperiment.utils.onTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateLoginCredentialsFragment : Fragment(R.layout.create_login_credentials_fragment) {
    private val viewModel by navGraphSavedStateViewModels(R.id.registration_graph) { savedStateHandle ->
        RegistrationViewModel(
            accessor<AuthenticationManager.Accessor>().get(),
            accessor<NavigationDispatcher.Accessor>().get(),
            savedStateHandle
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CreateLoginCredentialsFragmentBinding.bind(view)
        with(binding) {
            textUsername.setText(viewModel.username.value)
            textPassword.setText(viewModel.password.value)

            textUsername.onTextChanged { username -> viewModel.username.value = username }
            textPassword.onTextChanged { password -> viewModel.password.value = password }

            viewModel.isRegisterAndLoginEnabled.observe(viewLifecycleOwner) { enabled ->
                buttonRegisterAndLogin.isEnabled = enabled
            }
            buttonRegisterAndLogin.onClick { viewModel.onRegisterAndLoginClicked() }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onCreateLoginCredentialsBackEvent()
                }
            }
        )
    }
}