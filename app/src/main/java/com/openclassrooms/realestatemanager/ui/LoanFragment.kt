package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.fragment_loan.*

class LoanFragment : Fragment(R.layout.fragment_loan) {

    private var result: Float = 0f
    private var resultPerYear: Float = 0f
    private var resultPerMonth: Float = 0f
    private var nbrYear: Editable? = null
    private var isDollar: Boolean = false
    private lateinit var menu: Menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        tv_total_amount.text = resources.getString(R.string.total_amount, "", "");
        tv_per_year.text = resources.getString(R.string.per_year, "", "");
        tv_per_month?.text = resources.getString(R.string.per_month, "", "");

        val loanAmount = et_loan_amount.text
        val interestedRate = et_interest_rate.text
        nbrYear = et_number_years.text

        btn_calculate.setOnClickListener {
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
                        et_loan_amount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
                    } else {
                        et_loan_amount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
                        result = Utils.convertEuroToDollar(result.toInt()).toFloat()
                        resultPerYear = result / nbrYear.toString().toInt()
                        resultPerMonth = resultPerYear / 12
                        showResults("$")
                    }
                } else {
                    isDollar = true
                    menu.getItem(0).setIcon(R.drawable.ic_dollar)
                    if (result == 0f) {
                        et_loan_amount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
                    } else {
                        et_loan_amount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
                        result = Utils.convertDollarToEuro(result.toInt()).toFloat()
                        resultPerYear = result / nbrYear.toString().toInt()
                        resultPerMonth = resultPerYear / 12
                        showResults("€")
                    }
                }
            }
            R.id.tb_menu_logout -> Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResults(currency : String) {
        tv_total_amount.text = resources.getString(R.string.total_amount, currency, String.format("%.0f", result));
        tv_per_year.text = resources.getString(R.string.per_year, currency, String.format("%.2f", resultPerYear));
        tv_per_month.text = resources.getString(R.string.per_month, currency, String.format("%.2f", resultPerMonth));
    }
}