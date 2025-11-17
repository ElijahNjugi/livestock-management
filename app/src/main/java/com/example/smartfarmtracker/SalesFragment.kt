package com.example.smartfarmtracker

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.model.SalesEntry
import com.example.smartfarmtracker.ui.adapters.SalesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class SalesFragment : Fragment() {

    companion object {
        private const val ARG_ANIMAL_ID = "animalId"
        private const val ARG_ANIMAL_TYPE = "animalType"

        fun newInstance(animalId: String, animalType: String) = SalesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ANIMAL_ID, animalId)
                putString(ARG_ANIMAL_TYPE, animalType)
            }
        }
    }

    private lateinit var animalId: String
    private lateinit var animalType: String
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalesAdapter
    private val items = mutableListOf<SalesEntry>()
    private var listener: ListenerRegistration? = null
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animalId = arguments?.getString(ARG_ANIMAL_ID) ?: ""
        animalType = arguments?.getString(ARG_ANIMAL_TYPE) ?: ""
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_sales, container, false)
        recyclerView = v.findViewById(R.id.rvSales)
        fabAdd = v.findViewById(R.id.fabAddSales)

        adapter = SalesAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fabAdd.setOnClickListener {
            AddSalesDialog.newInstance(animalId, animalType).show(childFragmentManager, "AddSale")
        }

        subscribeSales()

        return v
    }

    private fun subscribeSales() {
        val uid = auth.currentUser?.uid ?: return
        val salesRef = db.collection("users").document(uid)
            .collection("sales")
            .orderBy("dateOfSale")

        listener = salesRef.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            items.clear()
            snapshot?.documents?.forEach { doc ->
                val s = doc.toObject(SalesEntry::class.java)
                if (s != null) {
                    if (s.id.isBlank()) s.id = doc.id
                    items.add(s)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.remove()
    }
}
