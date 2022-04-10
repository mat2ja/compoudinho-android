package android.tvz.hr.kalkulatorosrecki

import android.content.res.Configuration
import android.os.Bundle
import android.tvz.hr.kalkulatorosrecki.databinding.ActivityMainBinding
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.text.DecimalFormat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            inputYears.setText(10.toString())

            buttonCalculate.setOnClickListener {
                calculateInvestment(view)
            }

            setTheme()

            setTextSizeProgressBar()
        }
    }

    private fun ActivityMainBinding.setTheme() {
        val mode =
            applicationContext?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {}
            Configuration.UI_MODE_NIGHT_NO -> {}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }

        val isNightMode = mode == Configuration.UI_MODE_NIGHT_YES
        darkModeSwitch.isChecked = isNightMode

        darkModeSwitch.setOnClickListener(
            View.OnClickListener {
                if (darkModeSwitch.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        )
    }


    fun calculateInvestment(view: View) {

        if (!validateInputs()) return

        binding.apply {
            val startingAmount = inputStartingAmount.text.toString().toDouble()
            val years = inputYears.text.toString().toInt()
            val interestRate = inputInterestRate.text.toString().toDouble()


            val df = DecimalFormat("#,###.##")
            val total = df.format(calcCompoundInterest(startingAmount, interestRate / 100, years))

            calculationTotal.text = "$$total"
        }
    }


    private fun ActivityMainBinding.setTextSizeProgressBar() {
        textSizeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val texzSize = when (progress) {
                    0 -> R.style.FontSizeXSmall
                    1 -> R.style.FontSizeSmall
                    2 -> R.style.FontSizeMedium
                    3 -> R.style.FontSizeLarge
                    4 -> R.style.FontSizeXLarge
                    else -> R.style.FontSizeMedium
                }
                calculationTotal.setTextAppearance(this@MainActivity, texzSize)
                calculationTotalLabel.setTextAppearance(this@MainActivity, texzSize)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }


    private fun calcCompoundInterest(P: Double, r: Double, t: Int, n: Int = 1): Double {
        return P * (1 + r / n).pow((n * t).toDouble())
    }

    private fun validateInputs(): Boolean {
        resetErrors();

        var valid = true
        binding.apply {
            if (inputStartingAmount.text.toString().isEmpty()) {
                inputStartingAmount.error = getString(R.string.starting_amount_required)
                valid = false
            } else if (inputStartingAmount.text.toString().toDouble() < 0) {
                inputStartingAmount.error = getString(R.string.starting_amount_positive)
                valid = false
            }

            if (inputInterestRate.text.toString().isEmpty()) {
                inputInterestRate.error = getString(R.string.interest_rate_required)
                valid = false
            }

            if (inputYears.text.toString().isEmpty()) {
                inputYears.error = getString(R.string.years_required)
                valid = false
            } else if (inputYears.text.toString().toInt() < 0) {
                inputYears.error = getString(R.string.years_positive)
                valid = false
            }
        }

        if (!valid) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalid_calc_msg),
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.successful_calc_msg),
                Toast.LENGTH_LONG
            ).show()
        }

        return valid
    }


    private fun resetErrors() {
        binding.apply {
            inputStartingAmount.error = null
            inputInterestRate.error = null
            inputYears.error = null
        }
    }


}