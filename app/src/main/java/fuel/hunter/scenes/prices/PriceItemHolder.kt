package fuel.hunter.scenes.prices

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fuel.hunter.R
import fuel.hunter.data.FuelCategory
import fuel.hunter.data.FuelPrice
import fuel.hunter.data.Item

class PriceItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
        when (item) {
            is FuelPrice -> {
                view.findViewById<TextView>(R.id.title).text = item.title
                view.findViewById<TextView>(R.id.address).text = item.address
                view.findViewById<TextView>(R.id.price).text = item.price.toString()
                view.findViewById<ImageView>(R.id.icon).setImageResource(item.logo)
            }
            is FuelCategory -> {
                view.findViewById<TextView>(R.id.header).text = item.name
            }
        }
    }
}