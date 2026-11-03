package com.pedro.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.pedro.task.R
import com.pedro.task.data.model.Status
import com.pedro.task.data.model.Task
import com.pedro.task.databinding.FragmentFormTaskBinding
import com.pedro.task.databinding.FragmentRecoverAccountBinding
import com.pedro.task.databinding.FragmentRegisterBinding
import com.pedro.task.util.initToolbar
import com.pedro.task.util.showBottomSheet

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val viewModel: TaskViewModel by activityViewModels()
    private val args: FormTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        reference = Firebase.database.reference
        auth = Firebase.auth

        getArgs()
        initListener()
    }

    private fun getArgs() {
        args.task.let {
            if (it != null) {
                this.task = it
                configTask()
            }
        }
    }

    private fun configTask(){
        newTask = false
        status = task.status
        binding.textToolbar.setText(R.string.text_toolbar_update_form_task_fragment)

        binding.editTextDescricao.setText(task.description)
        setStatus()
    }

    private fun setStatus(){
        val id = when (task.status){
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else -> R.id.rbDone
        }
        binding.radioGroup.check(id)
    }

    private fun initListener(){
        // Evento que monitora a mudança de escolha do radioGroup
        binding.radioGroup.setOnCheckedChangeListener { _, id -> status =
            when(id){
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData(){
        val description = binding.editTextDescricao.text.toString().trim()

        if (description.isNotBlank()){
            binding.progressBar.isVisible = true

            if (newTask) {
                task = Task(id = reference.database.reference.push().key ?: "",
                    description = description,
                    status = status)
            }

            saveTask()

        }else{
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }

    private fun saveTask(){
        reference
            .child("task")
            .child(auth.currentUser?.uid?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if (result.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT).show()

                    if (newTask){
                        // Criando nova tarefa
                        findNavController().popBackStack()
                    }else{
                        // Editando tarefa
                        Toast.makeText(
                            requireContext(),
                            R.string.text_update_sucess_form_task_fragment,
                            Toast.LENGTH_SHORT
                        ).show()

                        viewModel.setUpdateTask(task)
                        binding.progressBar.isVisible = false
                    }
                }else{
                    // Mostra erro específico se disponível
                    binding.progressBar.isVisible = false
                    val errorMessage = result.exception?.message?: getString(R.string.error_generic)
                    showBottomSheet(message = getString(R.string.error_generic))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}