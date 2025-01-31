package VasiliyGubenkov.Liters.DB

import VasiliyGubenkov.Liters.Mutable
import VasiliyGubenkov.Liters.R
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class MyAdapter(listMain:ArrayList<ListItem>, contextM: Context): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    
    lateinit var textView37: TextView


    class MyHolder(itemView: View, contextV: Context, listArray: ArrayList<ListItem>) : RecyclerView.ViewHolder(itemView) {

        var textView37 = itemView.findViewById<TextView>(R.id.textView37)
        var context = contextV
        var listArry = listArray


        fun setData (item: ListItem) {

            MaxMileageForEdit.maxMileage = (listArry.maxByOrNull { it.mileage }?.mileage ?: 0).toString()

            var litersMod = item.liters /100.0
            var amountMod = item.amount / 100.0

            var dateForString = convertDateToNormalFormat(item.date)
            var amountForString = String.format("%.2f", amountMod)
            var litersForString = String.format("%.2f", litersMod)
            var mileageForString = item.mileage.toString()

            val spDateForString = SpannableStringBuilder()
            val startDateForString = spDateForString.length

            spDateForString.append("$dateForString  ")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(16, true),
                startDateForString,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spDateForString.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD), // Жирный шрифт
                startDateForString,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val startAmount = spDateForString.length

            spDateForString.append("$amountForString")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(16, true),
                startAmount,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spDateForString.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD), // Жирный шрифт
                startAmount,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val startDollars = spDateForString.length

            spDateForString.append("${context.getString(R.string.dollars)} ")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(14, true),
                startDollars,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val startLitersForString = spDateForString.length

            spDateForString.append("$litersForString")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(16, true),
                startLitersForString,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spDateForString.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD), // Жирный шрифт
                startLitersForString,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val startLiters = spDateForString.length

            spDateForString.append("${context.getString(R.string.liters)} ")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(14, true),
                startLiters,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val startMileageForString = spDateForString.length
            spDateForString.append("$mileageForString")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(16, true),
                startMileageForString,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spDateForString.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD), // Жирный шрифт
                startMileageForString,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val startMiles = spDateForString.length

            spDateForString.append("${context.getString(R.string.miles)} ")
            spDateForString.setSpan(
                android.text.style.AbsoluteSizeSpan(14, true),
                startMiles,
                spDateForString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            textView37.text = spDateForString

            itemView.setOnClickListener {
                var intent = Intent(context, Mutable::class.java).apply {
                    putExtra(MyIntentConstants.I_DATE_KEY, item.date)
                    putExtra(MyIntentConstants.I_AMOUNT_KEY, String.format("%.2f", amountMod))
                    putExtra(MyIntentConstants.I_LITERS_KEY, String.format("%.2f", litersMod))
                    putExtra(MyIntentConstants.I_MILEAGE_KEY, item.mileage.toString())
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }


        private fun convertDateToDbFormat(date: String): String {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            return outputFormat.format(parsedDate ?: return date)
        }


        private fun convertDateToNormalFormat(date: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            return outputFormat.format(parsedDate ?: return date)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item2, parent, false), context, listArray)
    }


    override fun getItemCount(): Int {
        return listArray.size
    }


    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }


    fun updateAdapter(listItems: List<ListItem>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }


    fun removeItem(pos: Int, dbManager: MyDbManager) {
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}