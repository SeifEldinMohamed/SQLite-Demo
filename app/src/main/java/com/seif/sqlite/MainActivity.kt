package com.seif.sqlite

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.seif.sqlite.databinding.ActivityMainBinding
import com.seif.sqlite.models.EmpModelClass
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnAdd.setOnClickListener {
            addRecord()
        }

        setupListofDataIntoRecyclerView()
    }

    /**
     * Function is used to show the list of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {
            binding.rvItemsList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            binding.rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            binding.rvItemsList.adapter = itemAdapter
        } else { // No records saved yet
            binding.rvItemsList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Function is used to get the Items List from the database table.
     */
    private fun getItemsList(): ArrayList<EmpModelClass> {
        // creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        // calling the viewEmployee method of DatabaseHandler class to read the records
        return databaseHandler.viewEmployee()
    }

    // Method for saving the employee records in database
    private fun addRecord() {
        val name = binding.etName.text.toString()
        val email = binding.etEmailId.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (name.isNotEmpty() && email.isNotEmpty()) {
            val status = databaseHandler.addEmployee(EmpModelClass(0, name, email))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved successfully", Toast.LENGTH_LONG).show()
                binding.etName.text.clear()
                binding.etEmailId.text.clear()

                setupListofDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(
                    this,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Method is used to show the Custom Dialog.
     */
    fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this,R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(empModelClass.name)
        updateDialog.etUpdateEmailId.setText(empModelClass.email)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmailId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val status = databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated successfully", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()
                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                        this,
                        "Name or Email cannot be blank",
                        Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    /**
     * Method is used to show the Alert Dialog.
     */
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${empModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(
                        applicationContext,
                        "Record deleted successfully.",
                        Toast.LENGTH_LONG
                ).show()
                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

}