package com.aroundseattle.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.aroundseattle.AppExecutors
import com.aroundseattle.api.FoursquareDataProvider
import com.aroundseattle.data.foursquare.Venue
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroundseattle.R


/**
 * autocomplete adapter that uses the Foursquare API for venue suggestions
 */
class AutocompleteAdapter (
        context: Context,
        val dataProvider: FoursquareDataProvider) :
        ArrayAdapter<Venue>(context, R.layout.dropdown_item_2line),
        Filterable {
    private var list: ArrayList<Venue> = ArrayList()

    override fun getCount() = list.size

    override fun getItem(position: Int): Venue? {
        if (position >= 0 && position < list.size)
            return list.get(position)
        return null
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence) : Filter.FilterResults {
                val results = Filter.FilterResults()
                if (constraint.length > 0) {
                    val response = dataProvider.suggestCompletion(constraint.toString(),10)
                    if (response?.response != null && response.response.minivenues != null) {
                        val list:ArrayList<Venue> = ArrayList()
                        for (venue: Venue in response.response.minivenues) {
                            list.add(venue)
                        }
                        results.values = list
                        results.count  = list.size
                    }
                }
                return results;
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list.clear()
                if (results != null && results.count > 0) {
                    list = results.values as ArrayList<Venue>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.dropdown_item_2line, parent, false)
        }
        val item = getItem(position)
        (view!!.findViewById(R.id.text1) as TextView).setText(item?.name)
        (view.findViewById(R.id.text2) as TextView).setText(item?.location?.address)
        return view
    }
}
