package ca.qc.cgodin.miniprojet3.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.qc.cgodin.miniprojet3.databinding.SucursaleBinding
import ca.qc.cgodin.miniprojet3.network.Succursale

class SuccursaleAdapter(
    private var items: List<Succursale> = emptyList(),
    private val onDeleteClick: (Succursale) -> Unit
) : RecyclerView.Adapter<SuccursaleAdapter.ViewHolder>() {

    class ViewHolder(val binding: SucursaleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SucursaleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val succursale = items[position]
        holder.binding.tvVille.text = succursale.Ville
        holder.binding.tvBudget.text = "Budget: ${succursale.Budget}$"

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(succursale)

        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<Succursale>) {
        items = newList
        notifyDataSetChanged()
    }
}
