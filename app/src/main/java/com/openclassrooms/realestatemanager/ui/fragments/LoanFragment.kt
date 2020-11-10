package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentLoanBinding
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.DecimalFormat

class LoanFragment : Fragment(R.layout.fragment_loan) {

    private lateinit var binding: FragmentLoanBinding
    private var result: Float = 0f
    private var resultPerYear: Float = 0f
    private var resultPerMonth: Float = 0f
    private var nbrYear: Editable? = null
    private var isDollar: Boolean = false
    private lateinit var menu: Menu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoanBinding.bind(view)

        setHasOptionsMenu(true)

        binding.tvTotalAmount.text = resources.getString(R.string.total_amount, "", "");
        binding.tvPerYear.text = resources.getString(R.string.per_year, "", "");
        binding.tvPerMonth.text = resources.getString(R.string.per_month, "", "");

        val loanAmount = binding.etLoanAmount.text
        val interestedRate = binding.etInterestRate.text
        nbrYear = binding.etNumberYears.text

        binding.btnCalculate.setOnClickListener {
            if (loanAmount.isNullOrEmpty() || interestedRate.isNullOrEmpty() || nbrYear.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val interestedRateYear = interestedRate.toString().toFloat() / 100
            result = loanAmount.toString().toInt() * (1 + (interestedRateYear * nbrYear.toString().toInt()))
            resultPerYear = result / nbrYear.toString().toInt()
            resultPerMonth = resultPerYear / 12

            if (isDollar) {
                showResults("€")
            } else {
                showResults("$")
            }
        }
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_currency -> {
                if (isDollar) {
                    isDollar = false
                    menu.getItem(0).setIcon(R.drawable.ic_euro)
                    if (result == 0f) {
                        binding.etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
                    } else {
                        binding.etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
                        result = Utils.convertEuroToDollar(result.toInt()).toFloat()
                        resultPerYear = result / nbrYear.toString().toInt()
                        resultPerMonth = resultPerYear / 12
                        showResults("$")
                    }
                } else {
                    isDollar = true
                    menu.getItem(0).setIcon(R.drawable.ic_dollar)
                    if (result == 0f) {
                        binding.etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
                    } else {
                        binding.etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
                        result = Utils.convertDollarToEuro(result.toInt()).toFloat()
                        resultPerYear = result / nbrYear.toString().toInt()
                        resultPerMonth = resultPerYear / 12
                        showResults("€")
                    }
                }
            }
            R.id.tb_menu_logout -> {
                findNavController().navigate(R.id.logoutFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResults(currency: String) {
        val formatter = DecimalFormat("#,###.00")

        binding.tvTotalAmount.text = resources.getString(R.string.total_amount, currency, formatter.format(result));
        binding.tvPerYear.text = resources.getString(R.string.per_year, currency, formatter.format(resultPerYear));
        binding.tvPerMonth.text = resources.getString(R.string.per_month, currency, formatter.format(resultPerMonth));
    }
}