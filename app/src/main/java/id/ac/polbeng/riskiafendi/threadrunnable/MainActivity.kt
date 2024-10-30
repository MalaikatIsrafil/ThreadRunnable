package id.ac.polbeng.riskiafendi.threadrunnable

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import id.ac.polbeng.riskiafendi.threadrunnable.databinding.ActivityMainBinding

import android.widget.Toast
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
   //2 private lateinit var mHandler: Handler

    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val maxCounter = workDataOf(MyWorker.COUNTER to 20)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
        val myWorker = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(maxCounter)
            .setConstraints(constraints)
            .build()
        binding.button.setOnClickListener {
            workManager.enqueueUniqueWork(
                "oneTimeRequest",
                ExistingWorkPolicy.KEEP,
                myWorker
            )
        }
        WorkManager.getInstance(applicationContext)
            // requestId is the WorkRequest id
            .getWorkInfoByIdLiveData(myWorker.id)
            .observe(this) { workInfo: WorkInfo? ->
                if (workInfo != null) {
                    val progress = workInfo.progress
                    val value = progress.getInt(MyWorker.PROGRESS, 0)
                    binding.textView.text = value.toString()
                }
                if (workInfo != null && workInfo.state ==
                    WorkInfo.State.SUCCEEDED) {
                    val message =
                        workInfo.outputData.getString(MyWorker.MESSAGE)
                    Toast.makeText(this, message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    /*3
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)

          //  setContentView(R.layout.activity_main)
            setContentView(binding.root)

           //2 mHandler = MyHandler()

            binding.button.setOnClickListener {
                Worker().execute()
                /*2
                Thread {
                    killSomeTime()
                } .start()


                 */

               /*1
                val runnable = Worker()
                val thread = Thread(runnable)
                thread.start()

                */
            }
        }
        /*1
        inner class Worker : Runnable {
            override fun run() {
                killSomeTime()
            }
        }

         */

        @SuppressLint("StaticFieldLeak")
        inner class Worker : AsyncTask<Void, String, Boolean>() {
            override fun doInBackground(vararg p0: Void?): Boolean {
                for (i in 1..20) {
                    publishProgress(i.toString())
                    Thread.sleep(2000)
                }
                return true
            }
            override fun onProgressUpdate(vararg values: String?) {
                binding.textView.text = values[0]
            }
            override fun onPostExecute(result: Boolean?) {
                println(result)
            }
        }


      /*
        @SuppressLint("HandlerLeak")
        inner class MyHandler : Handler() {
            override fun handleMessage(msg: Message) {
                binding.textView.text = msg.data?.getString("counter")
            }
        }


        private fun killSomeTime() {
            for (i in 1..20) {
                runOnUiThread{
                    binding.textView.text = i.toString()
                }
                val msg = Message.obtain()
                msg.data.putString("counter", i.toString())
                mHandler.sendMessage(msg)

                Thread.sleep(2000)
                println("i: $i")
            }
        }

       */


     */
}