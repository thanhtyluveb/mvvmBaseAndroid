package com.example.thefirstprojecttdtdemo.ui.screens.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.navigation.fragment.findNavController
import com.example.thefirstprojecttdtdemo.R
import com.example.thefirstprojecttdtdemo.network.WebSocketNetWork
import com.example.thefirstprojecttdtdemo.ui.common.BaseFragment
import com.example.thefirstprojecttdtdemo.ui.common.ProgressDialogFragment
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_forth.*
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : BaseFragment() {
    private var mCurrentHour: Int = -1
    private var mOpenOfSession: Float? = null
    private var mHighValueOfSession: Float = 0f
    private var mLowValueOfSession: Float = Float.MAX_VALUE
    private lateinit var data: CombinedData
    private var entries: ArrayList<CandleEntry> = ArrayList()
    private var set: CandleDataSet? = null
    private var param1: String? = null
    private var param2: String? = null
    private var mWebSocketNetWork = WebSocketNetWork()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvGotoHome.setOnClickListener(this)
        val fm = activity?.supportFragmentManager
        fm?.apply {
            beginTransaction().add(android.R.id.content, ProgressDialogFragment.newInstance())
                .addToBackStack(ProgressDialogFragment.TAG)
                .commit()
        }
        initDataChart()
        prepareChart()
        mWebSocketNetWork.setUpCallBack { bitCoinModel ->
            bitCoinModel?.price?.toFloat()?.let { price ->
                if (mCurrentHour != Calendar.getInstance()[Calendar.HOUR]) {
                    mOpenOfSession =
                        if (mCurrentHour - 1 in entries.indices) entries[mCurrentHour - 1].close else price
                    mCurrentHour = Calendar.getInstance()[Calendar.HOUR]
                }
                if (price > mHighValueOfSession) mHighValueOfSession = price
                if (price < mLowValueOfSession) mLowValueOfSession = price
                activity?.runOnUiThread {
                    updateDataToChart(price)
                    setUpBtcPriceText(price)
                }
            }
            fm?.popBackStack(ProgressDialogFragment.TAG, POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onResume() {
        super.onResume()
        mWebSocketNetWork.initWebSocket()
        mWebSocketNetWork.connect()
    }

    private fun prepareChart() {
        cbChart.apply {
            description.isEnabled = false
            setBackgroundColor(Color.WHITE)
            setDrawBarShadow(true)
            legend.isEnabled = false
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
            drawOrder = arrayOf<DrawOrder?>(DrawOrder.CANDLE)
        }

        cbChart.axisRight.isEnabled = false

        val leftAxis: YAxis = cbChart.axisLeft
        leftAxis.setDrawGridLines(false)

        val xAxis: XAxis = cbChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setLabelCount(12, false)
        xAxis.axisMaximum = 100f
        xAxis.axisLineColor = Color.BLACK

        data = CombinedData()
        data.setData(generateCandleData())
        data.setData(generateLineData())
        cbChart.setVisibleXRange(0f, 12f)
        cbChart.data = data
    }

    private fun generateLineData(): LineData? {
        val d = LineData()
        val entries =
            ArrayList<Entry>()
        for (index in 0 until 10) entries.add(
            Entry(
                index + 0.5f,
                index.toFloat() * 10
            )
        )
        val set = LineDataSet(entries, "Line DataSet")
        set.color = Color.rgb(240, 238, 70)
        set.lineWidth = 2.5f
        set.fillColor = Color.rgb(240, 238, 70)
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(240, 238, 70)
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }

    private fun generateCandleData(): CandleData? {
        val d = CandleData()
        set = CandleDataSet(entries, "BitCoin Chart")
        set?.apply {
            decreasingColor = Color.RED
            increasingColor = Color.GREEN
            valueTextSize = 10f
            setDrawValues(false)
            shadowColorSameAsCandle = true
        }
        d.addDataSet(set)
        return d
    }

    override fun onPause() {
        super.onPause()
        mWebSocketNetWork.close()
    }

    private fun initDataChart() {
        val calendar = Calendar.getInstance()
        for (hour in calendar.getActualMinimum(Calendar.HOUR_OF_DAY)..calendar.getActualMaximum(
            Calendar.HOUR_OF_DAY
        )) {
            entries.add(CandleEntry(hour.toFloat(), 0f, 0f, 0f, 0f))
        }
    }

    private fun updateDataToChart(price: Float) {
        if ((mCurrentHour - 1) in entries.indices) {
            entries[mCurrentHour - 1] = (CandleEntry(
                mCurrentHour.toFloat(),
                mHighValueOfSession,
                mLowValueOfSession,
                mOpenOfSession!!,
                price
            ))
            set?.notifyDataSetChanged()
            data.notifyDataChanged()
            cbChart.axisLeft.apply {
                this.axisMinimum = mLowValueOfSession - SHAWDOW_SIZE
                this.axisMaximum = mHighValueOfSession + SHAWDOW_SIZE
            }
            cbChart.notifyDataSetChanged()
            cbChart.invalidate()
        }
    }

    private fun setUpBtcPriceText(price: Float) {
        tvPriceBitCoinFragment4.text = "$price €"
        tvPriceBitCoinFragment4.setTextColor(getColor(price))
    }

    private fun getColor(price: Float) = if (mOpenOfSession ?: 0f > price
    ) Color.RED else Color.GREEN

    override fun onClick(v: View?) {
        this.findNavController().navigate(R.id.action_global_home2)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val SHAWDOW_SIZE = 100
    }

}