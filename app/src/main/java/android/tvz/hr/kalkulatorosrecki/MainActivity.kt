package android.tvz.hr.kalkulatorosrecki

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.tvz.hr.kalkulatorosrecki.databinding.ActivityMainBinding
import android.view.View
import android.widget.Toast
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.inputYears.setText(10.toString())

        binding.buttonCalculate.setOnClickListener {
            calculateInvestment(view)
        }
    }


    fun calculateInvestment(view: View) {

        if (!validateInputs()) return

        val startingAmount = binding.inputStartingAmount.text.toString().toDouble()
        val years = binding.inputYears.text.toString().toInt()
        val interestRate = binding.inputInterestRate.text.toString().toDouble()


        val df = DecimalFormat("#,###.##")
        val total = df.format(calcCompoundInterest(startingAmount, interestRate / 100, years))

        binding.calculationTotal.text = "$$total"
    }

    private fun calcCompoundInterest(P: Double, r: Double, t: Int, n: Int = 1): Double {
        return P * (1 + r / n).pow((n * t).toDouble())
    }

    private fun validateInputs(): Boolean {
        resetErrors();

        var valid = true
        if (binding.inputStartingAmount.text.toString().isEmpty()) {
            binding.inputStartingAmount.error = "Starting amount is required"
            valid = false
        } else if (binding.inputStartingAmount.text.toString().toDouble() < 0) {
            binding.inputStartingAmount.error = "Starting amount must be positive"
            valid = false
        }

        if (binding.inputInterestRate.text.toString().isEmpty()) {
            binding.inputInterestRate.error = "Interest rate is required"
                valid = false
        }

        if (binding.inputYears.text.toString().isEmpty()) {
            binding.inputYears.error = "Years is required"
            valid = false
        } else if (binding.inputYears.text.toString().toInt() < 0) {
            binding.inputYears.error = "Years must be positive"
            valid = false
        }

        if (!valid) {
            Toast.makeText(applicationContext, "Please fill in all required fields", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Mo Money, Mo Problems", Toast.LENGTH_LONG).show()
        }

        println("valid: $valid")
        return valid
    }


    private fun resetErrors() {
        binding.inputStartingAmount.error = null
        binding.inputInterestRate.error = null
        binding.inputYears.error = null
        binding.calculationTotal.text = resources.getString(R.string.total)
    }


}