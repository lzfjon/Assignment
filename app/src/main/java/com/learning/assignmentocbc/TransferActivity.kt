package com.learning.assignmentocbc

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.learning.assignmentocbc.model.TransferRequest
import com.learning.assignmentocbc.network.createApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransferActivity : AppCompatActivity() {

    lateinit var fromTransferToDashboard : Button
    lateinit var transferBack : ImageView

    lateinit var payeeOption : Spinner
    lateinit var editAmount : EditText
    lateinit var editDescription: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        fromTransferToDashboard = findViewById(R.id.buttonTransferNow)
        transferBack = findViewById(R.id.transferBack)

        payeeOption = findViewById(R.id.spinnerPayee)
        editAmount = findViewById(R.id.editAmount)
        editDescription = findViewById(R.id.editDescription)


        //get token
        val token = intent?.getStringExtra("token").toString()
        val accountHolder = intent?.getStringExtra("username")
        val accountNo = intent?.getStringExtra("accountNo")
        Log.d("Transfer Act On Create",token)


//        val payeeList = MutableLiveData<ArrayList<String>>()
        val payeeMap = MutableLiveData<MutableMap<String,String>>()
        GlobalScope.launch {

            //Extract Payee Details
            val payeeResponse = createApiService().getPayeesDetails(token).body()
            val payeeDetails = payeeResponse?.data
            Log.d("Transfer Activity",payeeDetails.toString())
//            val payeeListInfo = arrayListOf<String>()
            val payeeMapInfo = mutableMapOf<String,String>()

            for(pd in payeeDetails!!){
//                payeeListInfo.add(pd.payeeName!!)
                payeeMapInfo[pd.payeeName!!] = pd.accountNumber!!
            }
//            Log.d("Transfer Activity",payeeList.toString())
            Log.d("Transfer Activity",payeeMapInfo.toString())
//            payeeList.postValue(payeeListInfo)
            payeeMap.postValue(payeeMapInfo)
        }

//        payeeList.observe(this@TransferActivity, Observer {
//            val payeeAdapter = ArrayAdapter(this@TransferActivity, android.R.layout.simple_spinner_item, it)
//
//            payeeOption.adapter = payeeAdapter
//            payeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        })

        payeeMap.observe(this@TransferActivity, Observer {
            val nameList = arrayListOf<String>()
            for(item in it.keys){
                nameList.add(item)
            }


            val payeeAdapter = ArrayAdapter(this@TransferActivity, android.R.layout.simple_spinner_item, nameList)

            payeeOption.adapter = payeeAdapter
            payeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        })




        fromTransferToDashboard.setOnClickListener {
            var intentTransferNow = Intent(this@TransferActivity,DashboardActivity::class.java)

            //selection of map

            val selectedPayeeAccountNo : String = payeeMap.value?.get(payeeOption.selectedItem.toString())
                .toString()
            val amountInput  = editAmount.text.toString()
            val descriptionInput : String = editDescription.text.toString()
            val transferRequest = TransferRequest(selectedPayeeAccountNo,amountInput.toDouble(),descriptionInput)

            GlobalScope.launch {
                Log.d("TransferActivity", "transferRequest = $transferRequest")
                val response = createApiService().postTransfer(token,transferRequest)
                val transferResponse = response.body()

                Log.d("TransferActivity","transfer response = " + transferResponse.toString())
                if(transferResponse?.status == "success"){
                    Log.d("Transfer Activity", "successful")
                }

                Log.d("Transfer Activity","PayeeAccount = $selectedPayeeAccountNo")
                Log.d("Transfer Activity","amountInput = $amountInput")
                Log.d("Transfer Activity","descriptionInput = $descriptionInput")
            }

            intentTransferNow.putExtra("token",token)
            intentTransferNow.putExtra("accountNo",accountNo)
            intentTransferNow.putExtra("username",accountHolder)
            startActivity(intentTransferNow)
        }

        transferBack.setOnClickListener {
            var intentTransferBack = Intent(this@TransferActivity,DashboardActivity::class.java)
            intentTransferBack.putExtra("token",token)
            intentTransferBack.putExtra("accountNo",accountNo)
            intentTransferBack.putExtra("username",accountHolder)
            startActivity(intentTransferBack)
        }
    }
}