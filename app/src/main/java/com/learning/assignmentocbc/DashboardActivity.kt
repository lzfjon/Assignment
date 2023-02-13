package com.learning.assignmentocbc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.learning.assignmentocbc.model.*
import com.learning.assignmentocbc.network.createApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class DashboardActivity : AppCompatActivity() {

    lateinit var fromDashboardToTransfer : Button
    lateinit var logout : TextView

    lateinit var dashbdTotalAmount : TextView
    lateinit var dashbdAccountNo : TextView
    lateinit var dashbdAccountHolder : TextView
//
//    lateinit var transactionHistoryCard : TextView
    lateinit var transactionHistoryRecyclerView : RecyclerView
    lateinit var transactionHistoryAdapter : TransactionHistoryAdapter

//    lateinit var transactionDetailsList : List<TransactionDetails>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        //get token
        val token = intent?.getStringExtra("token").toString()

        //dashboard component
        fromDashboardToTransfer = findViewById(R.id.buttonMakeTransfer)
        logout = findViewById(R.id.textLogout)


        dashbdAccountNo = findViewById(R.id.dbUserAccountNumber)
        val accountNo = intent?.getStringExtra("accountNo").toString()
        dashbdAccountNo.text = accountNo

        dashbdAccountHolder = findViewById(R.id.dbNameAccountHolder)
        val accountHolder = intent?.getStringExtra("username").toString()
        dashbdAccountHolder.text =  accountHolder


        val accountBalance = MutableLiveData<String>()
//        val transactionHistoryList = MutableLiveData<ArrayList<ArrayList<String>>>()
        val transactionHistoryMap = MutableLiveData<
                MutableMap<String,
                        ArrayList<
                                ArrayList<String>
                                >
                        >
                >()



        GlobalScope.launch {

            //get balance information
            val balanceResponse = createApiService().getBalanceDetails(token).body()
            accountBalance.postValue(checkMoneyFormat(balanceResponse?.balance.toString()))

            //get Api Transaction Details
            val transactionResponse = createApiService().getTransactionDetails(token).body()
            val transactionDetails = transactionResponse?.data
            Log.d("Dashboard Activity",transactionDetails.toString())

            val historyMap = getHistoryMap(transactionDetails)
            transactionHistoryMap.postValue(historyMap)
            Log.d("Dashboard Activity", "historyMap = $historyMap")
        }

        dashbdTotalAmount = findViewById(R.id.dashbdTotalAmount)

        accountBalance.observe(this@DashboardActivity, Observer {
            dashbdTotalAmount.text = buildString {
                append("SGD ")
                append(it.toString())
            }
        })


        //transaction history
        transactionHistoryRecyclerView = findViewById(R.id.dateRecyclerView)
        transactionHistoryRecyclerView.layoutManager = LinearLayoutManager(this@DashboardActivity)

        transactionHistoryMap.observe(this@DashboardActivity, Observer {
            Log.d("Dashboard Activity","before datestring")
            val dateSet = it.keys
            val dateString = arrayListOf<String>()

            for(ds in dateSet){
                dateString.add(ds)
            }

            Log.d("Dashboard Activity", "dateString = $dateString")

            val detailSet = it.values.first()

            transactionHistoryAdapter = TransactionHistoryAdapter(
                dateString,
                transactionHistoryMap.value
            )
            transactionHistoryRecyclerView.adapter=transactionHistoryAdapter
        })


        //make transfer button
        fromDashboardToTransfer.setOnClickListener {

            var intentMakeTransfer = Intent(this@DashboardActivity,TransferActivity::class.java)
            intentMakeTransfer.putExtra("token",token)
            intentMakeTransfer.putExtra("accountNo",accountNo)
            intentMakeTransfer.putExtra("username",accountHolder)
            startActivity(intentMakeTransfer)
        }

        //logout button
        logout.setOnClickListener {

            var intentLogout = Intent(this@DashboardActivity,LoginActivity::class.java)

            startActivity(intentLogout)
        }
    }


    fun getHistoryMap(transactionDetails: List<TransactionDetails>?) : MutableMap<String,ArrayList<ArrayList<String>>>{

        var transactionHistoryMap: MutableMap<String,ArrayList<ArrayList<String>>> = mutableMapOf()
        for(td in transactionDetails!!) {

            val dateString = td.transactionDate.toString()
            val transactionDate = convertDateIntoFormat(dateString)
            var transactToName: String? = null
            var transactToAccountNo: String? = null
            var transactAmount = checkMoneyFormat(td.transactionAmount.toString())


            Log.d("Dashboard Activity", "transactAmount = $transactAmount")

            td.transactionReceipient?.accountHolder?.let{
                transactToName = td.transactionReceipient?.accountHolder
                transactToAccountNo = td.transactionReceipient?.accountNo
                transactAmount = "- $transactAmount"
                Log.d("Dashboard Activity", "transact to name in let =$transactToName")

            } ?: run {
                transactToName = td.sender?.accountHolder
                transactToAccountNo = td.sender?.accountNo
                Log.d("Dashboard Activity", "transact to name in run =$transactToName")
            }




            var transactionHistoryDetails : ArrayList<String> = arrayListOf(transactionDate,transactToName.toString(),transactToAccountNo.toString(),transactAmount)

            if(!transactionHistoryMap.contains(transactionDate)){
                transactionHistoryMap[transactionDate] = arrayListOf(transactionHistoryDetails)
            } else{
                var tempList = transactionHistoryMap[transactionDate]
                tempList!!.add(transactionHistoryDetails)
                transactionHistoryMap[transactionDate] = tempList
            }
        }
        Log.d("Dashboard Activity", "Map = $transactionHistoryMap")
        return transactionHistoryMap
    }

    fun convertDateIntoFormat(dateString: String) : String{
        val dateMonth = dateString.subSequence(3,7)
        val dateDay = dateString.subSequence(8,10)
        val dateYear = dateString.subSequence(dateString.length-5,dateString.length)
        return "$dateDay $dateMonth $dateYear"

    }


    fun checkMoneyFormat(amount:String) : String{

        val amountLength = amount.length
        if (amount.contains(".")){

            if(amountLength-amount.indexOf(".")==3){
                return amount
            }

            if(amountLength-amount.indexOf(".")==2){
                return amount+"0"
            }

        }

        return "$amount.00"
    }
}