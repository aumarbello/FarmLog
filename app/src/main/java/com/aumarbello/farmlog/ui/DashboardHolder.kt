package com.aumarbello.farmlog.ui

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aumarbello.farmlog.databinding.ItemDashboardBarChartBinding
import com.aumarbello.farmlog.databinding.ItemDashboardCountBinding
import com.aumarbello.farmlog.databinding.ItemDashboardPieChartBinding
import com.aumarbello.farmlog.models.DashboardItem
import com.aumarbello.farmlog.models.DashboardItem.*
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter

abstract class DashboardHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindItem(item: DashboardItem)

    class CountHolder(view: View) : DashboardHolder(view) {
        private val binding = ItemDashboardCountBinding.bind(view)

        override fun bindItem(item: DashboardItem) {
            with(item as CountItem) {
                binding.title.text = title
                binding.count.text = count.toString()
            }
        }
    }

    class BarHolder(view: View) : DashboardHolder(view) {
        private val binding = ItemDashboardBarChartBinding.bind(view)

        override fun bindItem(item: DashboardItem) {
            with(item as BarChartItem) {
                binding.title.text = title
                binding.barChart.apply {
                    description = null
                    axisLeft.isEnabled = false
                    axisRight.isEnabled = false
                    isDoubleTapToZoomEnabled = false
                    legend.isEnabled = false
                    xAxis.apply {
                        valueFormatter = object : ValueFormatter() {
                            val labels = item.items.keys.toList()
                            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                                return labels[value.toInt()]
                            }
                        }
                        position = XAxis.XAxisPosition.BOTTOM
                        gridColor = Color.TRANSPARENT
                    }

                    data = toBarData()
                    invalidate()
                }
            }
        }

        private fun BarChartItem.toBarData(): BarData {
            var pos = 0
            val entries = items.map {
                BarEntry(
                    pos++.toFloat(),
                    it.value.toFloat(),
                    it.key
                )
            }
            val dataSet = BarDataSet(entries, "").apply {
                colors = getColorsForGraph(1)
            }
            return BarData(dataSet).apply {
                barWidth = 0.9f
            }
        }
    }

    class PieHolder(view: View) : DashboardHolder(view) {
        private val binding = ItemDashboardPieChartBinding.bind(view)

        override fun bindItem(item: DashboardItem) {
            with(item as PieChartItem) {
                binding.title.text = title
                binding.pieChart.apply {
                    description = null
                    isRotationEnabled = false
                    data = toPieData()

                    setUsePercentValues(true)
                    setEntryLabelColor(Color.DKGRAY)

                    invalidate()
                }
            }
        }

        private fun PieChartItem.toPieData(): PieData {
            val entries = groups.map { PieEntry(it.value.toFloat(), it.key) }
            val dataSet = PieDataSet(entries, "").apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                        return "${value.toInt()}%"
                    }
                }
                valueTextSize = 16f
                colors = getColorsForGraph(entries.size)
            }

            return PieData(dataSet)
        }
    }

    private companion object {
        fun getColorsForGraph(numberOfColors: Int): List<Int> {
            val colors = listOf(Color.GRAY, Color.YELLOW, Color.RED, Color.BLUE)

            return colors.subList(0, numberOfColors)
        }
    }
}