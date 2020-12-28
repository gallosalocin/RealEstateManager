package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentLoanBinding
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.hideDetailsContainer
import com.openclassrooms.realestatemanager.utils.Utils.showDetailsContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow

class LoanFragment : Fragment(R.layout.fragment_loan) {

    private var _binding: FragmentLoanBinding? = null
    private val binding get() = _binding!!

    private var totalPayment: Float = 0f
    private var annualPayment: Float = 0f
    private var totalInterest: Float = 0f
    private var monthlyPayment: Float = 0f
    private var nbrYear: Int = 0
    private var isDollar: Boolean = true
    private lateinit var menu: Menu


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.loan_calculator)

        if (resources.getBoolean(R.bool.isTablet)) {
            hideDetailsContainer(requireActivity())
        }

        clearAllFields()

        binding.btnCalculate.setOnClickListener {
            if (!Utils.validateInputFieldIfNullOrEmpty(binding.etLoanAmount, "Can't be empty")
                    or (!Utils.validateInputFieldIfNullOrEmpty(binding.etInterestRate, "Can't be empty"))
                    or (!Utils.validateInputFieldIfNullOrEmpty(binding.etNumberYears, "Can't be empty"))
            ) return@setOnClickListener

            loanPaymentCalculation()
            if (isDollar) displayResultsInDollar() else displayResultsInEuro()
        }
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.custom_toolbar, menu)
        menu.getItem(0).isVisible = true
        menu.getItem(1).isVisible = true
        menu.getItem(2).isVisible = false
        menu.getItem(3).isVisible = false
        menu.getItem(4).isVisible = false
        menu.getItem(5).isVisible = true
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_currency -> {
                if (isDollar) {
                    menu.getItem(0).setIcon(R.drawable.ic_dollar)
                    if (totalPayment == 0f) {
                        binding.etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
                    } else {
                        convertResultsInEuro()
                    }
                    isDollar = !isDollar
                } else {
                    menu.getItem(0).setIcon(R.drawable.ic_euro)
                    if (totalPayment == 0f) {
                        binding.etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
                    } else {
                        convertResultsInDollar()
                    }
                    isDollar = !isDollar
                }
            }

            R.id.tb_menu_reload -> clearAllFields()

            R.id.tb_menu_logout -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_container, LogoutFragment())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loanPaymentCalculation() {
        val loanAmount = binding.etLoanAmount.text.toString().toInt()
        val interestedRate = binding.etInterestRate.text.toString().toFloat()
        nbrYear = binding.etNumberYears.text.toString().toInt()

        val interestedRateYear = interestedRate / 100
        val interestedRateMonth = interestedRateYear / 12
        val nbrMonth = nbrYear * 12

        monthlyPayment = ((loanAmount * interestedRateMonth) * (1 + interestedRateMonth).pow(nbrMonth)) / ((1 + interestedRateMonth).pow(nbrMonth) - 1)

        totalPayment = monthlyPayment * nbrMonth
        totalInterest = totalPayment - loanAmount
        annualPayment = totalPayment / nbrYear
    }

    private fun convertResultsInDollar() {
        val loanAmountConverted = Utils.convertEuroToDollar(binding.etLoanAmount.text.toString().toInt())
        totalPayment = Utils.convertEuroToDollar(totalPayment.toInt()).toFloat()
        totalInterest = Utils.convertEuroToDollar(totalInterest.toInt()).toFloat()
        annualPayment = Utils.convertEuroToDollar(annualPayment.toInt()).toFloat()
        monthlyPayment = Utils.convertEuroToDollar(monthlyPayment.toInt()).toFloat()


        binding.apply {
            etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
            etLoanAmount.setText(loanAmountConverted.toString())
            tvTotalPayment.text = resources.getString(R.string.total_amount, Utils.formatInDollar(totalPayment, 2))
            tvTotalInterest.text = resources.getString(R.string.total_interest, Utils.formatInDollar(totalInterest, 2))
            tvAnnualPayment.text = resources.getString(R.string.annual_payment, Utils.formatInDollar(annualPayment, 2))
            tvMonthlyPayment.text = resources.getString(R.string.monthly_payment, Utils.formatInDollar(monthlyPayment, 2))
        }
    }

    private fun convertResultsInEuro() {
        val loanAmountConverted = Utils.convertDollarToEuro(binding.etLoanAmount.text.toString().toInt())
        totalPayment = Utils.convertDollarToEuro(totalPayment.toInt()).toFloat()
        totalInterest = Utils.convertDollarToEuro(totalInterest.toInt()).toFloat()
        annualPayment = Utils.convertDollarToEuro(annualPayment.toInt()).toFloat()
        monthlyPayment = Utils.convertDollarToEuro(monthlyPayment.toInt()).toFloat()

        binding.apply {
            etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
            etLoanAmount.setText(loanAmountConverted.toString())
            tvTotalPayment.text = resources.getString(R.string.total_amount, Utils.formatInEuro(totalPayment, 2))
            tvTotalInterest.text = resources.getString(R.string.total_interest, Utils.formatInEuro(totalInterest, 2))
            tvAnnualPayment.text = resources.getString(R.string.annual_payment, Utils.formatInEuro(annualPayment, 2))
            tvMonthlyPayment.text = resources.getString(R.string.monthly_payment, Utils.formatInEuro(monthlyPayment, 2))
        }
    }

    private fun displayResultsInDollar() {
        binding.apply {
            etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar, 0, 0, 0)
            tvTotalPayment.text = resources.getString(R.string.total_amount, Utils.formatInDollar(totalPayment, 2))
            tvTotalInterest.text = resources.getString(R.string.total_interest, Utils.formatInDollar(totalInterest, 2))
            tvAnnualPayment.text = resources.getString(R.string.annual_payment, Utils.formatInDollar(annualPayment, 2))
            tvMonthlyPayment.text = resources.getString(R.string.monthly_payment, Utils.formatInDollar(monthlyPayment, 2))
        }
    }

    private fun displayResultsInEuro() {
        binding.apply {
            etLoanAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro, 0, 0, 0)
            tvTotalPayment.text = resources.getString(R.string.total_amount, Utils.formatInEuro(totalPayment, 2))
            tvTotalInterest.text = resources.getString(R.string.total_interest, Utils.formatInEuro(totalInterest, 2))
            tvAnnualPayment.text = resources.getString(R.string.annual_payment, Utils.formatInEuro(annualPayment, 2))
            tvMonthlyPayment.text = resources.getString(R.string.monthly_payment, Utils.formatInEuro(monthlyPayment, 2))
        }
    }

    private fun clearAllFields() {
        binding.apply {
            etLoanAmount.text?.clear()
            etInterestRate.text?.clear()
            etNumberYears.text?.clear()
            tvTotalPayment.text = resources.getString(R.string.total_amount, "")
            tvTotalInterest.text = resources.getString(R.string.total_interest, "")
            tvAnnualPayment.text = resources.getString(R.string.annual_payment, "")
            tvMonthlyPayment.text = resources.getString(R.string.monthly_payment, "")
        }
    }

    override fun onStop() {
        super.onStop()
        if (resources.getBoolean(R.bool.isTablet)) {
            showDetailsContainer(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}