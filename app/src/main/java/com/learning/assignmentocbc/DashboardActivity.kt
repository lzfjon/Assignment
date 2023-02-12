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
            accountBalance.postValue(balanceResponse?.balance.toString())

            //get Api Transaction Details
            val transactionResponse = createApiService().getTransactionDetails(token).body()
            val transactionDetails = transactionResponse?.data
            Log.d("Dashboard Activity",transactionDetails.toString())

//            val dateString = getDateString(transactionDetails)
//            Log.d("Dashboard Activity", "dateString = $dateString")
//            dateList.postValue(dateString)

//            val historyList = getHistoryList(transactionDetails)
//            transactionHistoryList.postValue(historyList)
//            Log.d("Dashboard Activity", "historyList = $historyList")


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
//            val detailList = arrayListOf<ArrayList<String>>()
//            for(dl in detailSet){
//                detailList.add(dl)
//            }

            transactionHistoryAdapter = TransactionHistoryAdapter(
                dateString,
                transactionHistoryMap.value
            )
            transactionHistoryRecyclerView.adapter=transactionHistoryAdapter
        })



//        transactionHistoryList.observe(this@DashboardActivity, Observer {
//            val dateString = getDateString(it)
//            transactionHistoryAdapter = TransactionHistoryAdapter(dateString,this@DashboardActivity)
//
//            transactionHistoryRecyclerView.adapter=transactionHistoryAdapter
//        })



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
            val transactToName = td.transactionReceipient?.accountHolder.toString()
            val transactToAccountNo = td.transactionReceipient?.accountNo.toString()
            val transactAmount = td.transactionAmount.toString()
            var transactionHistoryDetails : ArrayList<String> = arrayListOf(transactionDate,transactToName,transactToAccountNo,transactAmount)

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



    fun getHistoryList(transactionDetails: List<TransactionDetails>?) : ArrayList<ArrayList<String>>{

        var transactionHistoryDateList : ArrayList<ArrayList<String>> = arrayListOf()

        for(td in transactionDetails!!){

            val dateString = td.transactionDate.toString()
            val transactionDate = convertDateIntoFormat(dateString)
            val transactToName = td.transactionReceipient?.accountHolder.toString()
            val transactToAccountNo = td.transactionReceipient?.accountNo.toString()
            val transactAmount = td.transactionAmount.toString()
            var transactionHistoryDetails : ArrayList<String> = arrayListOf(transactionDate,transactToName,transactToAccountNo,transactAmount)

            transactionHistoryDateList.add(transactionHistoryDetails)

        }
        return transactionHistoryDateList
    }


    fun getDateString(transactionHistoryList: ArrayList<ArrayList<String>>) : ArrayList<String>{

        var transactionHistoryDateList : ArrayList<String> = arrayListOf()

        val listSize = transactionHistoryList.size
        for(i in 0 until transactionHistoryList.size){

            val dateDashboard = transactionHistoryList[i][0]


            if(!transactionHistoryDateList.contains(dateDashboard)){

                transactionHistoryDateList.add(dateDashboard)

                Log.d("Db Act", "transactionHistoryDataList = $dateDashboard")
            }
        }
        Log.d("Db Act", "transactionHistoryDataList = $transactionHistoryDateList")
        return transactionHistoryDateList
    }


    fun convertDateIntoFormat(dateString: String) : String{
        val dateMonth = dateString.subSequence(3,7)
        val dateDay = dateString.subSequence(8,10)
        val dateYear = dateString.subSequence(dateString.length-5,dateString.length)
        return "$dateDay $dateMonth $dateYear"

    }
}