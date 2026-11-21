package com.km.painel

import android.content.Context
import android.graphics.*
import android.view.*
import android.widget.*

class PanelView(context: Context) : LinearLayout(context) {

    var espAtivo = false
    var espLinha = false
    var espCaixa = false
    var espEsqueleto = false
    var espNome = false
    var espVida = false
    var aimbotAtivo = false
    var puxarParedes = false
    var exibirFov = false
    var exibirLinha = false
    var fovValue = 180f
    var ossoPuxada = "Cabeça"
    var tipoAimbot = "Ao Atirar"
    var aimKill = false
    var teleport = false
    var voarPlayer = false
    var speed = false
    var noRecoil = false

    private val config = ConfigManager(context)
    private val inputInjector = InputInjector(context)
    private val aimbotEngine = AimbotEngine(context, inputInjector)

    private val TAB_AIMBOT = 0
    private val TAB_ESP = 1
    private val TAB_CONFIG = 2
    private val TAB_INFO = 3
    private var currentTab = TAB_AIMBOT
    private lateinit var contentArea: LinearLayout

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#CC000000"))
        minimumWidth = 320.dp
        buildUI()
        loadConfig()
    }

    private fun buildUI() {
        val header = LinearLayout(context).apply {
            orientation = HORIZONTAL
            setBackgroundColor(Color.parseColor("#CC1a1a1a"))
            gravity = Gravity.CENTER_VERTICAL
            setPadding(12.dp, 8.dp, 12.dp, 8.dp)
        }
        val title = TextView(context).apply {
            text = "KM PAINEL"
            setTextColor(Color.WHITE)
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LayoutParams(0, WRAP_CONTENT, 1f)
        }
        val closeBtn = TextView(context).apply {
            text = "✕"
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(8.dp, 0, 0, 0)
            setOnClickListener {
                (context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)
                    ?.removeView(this@PanelView)
            }
        }
        header.addView(title)
        header.addView(closeBtn)
        addView(header)

        contentArea = LinearLayout(context).apply {
            orientation = VERTICAL
            setPadding(12.dp, 8.dp, 12.dp, 8.dp)
        }
        addView(contentArea)

        val tabBar = LinearLayout(context).apply {
            orientation = HORIZONTAL
            setBackgroundColor(Color.parseColor("#CC111111"))
            setPadding(8.dp, 6.dp, 8.dp, 6.dp)
        }
        listOf("🎯", "👁", "⚙", "ℹ").forEachIndexed { i, icon ->
            val tab = TextView(context).apply {
                text = icon
                textSize = 18f
                gravity = Gravity.CENTER
                setPadding(12.dp, 6.dp, 12.dp, 6.dp)
                layoutParams = LayoutParams(0, WRAP_CONTENT, 1f)
                setBackgroundColor(if (i == 0) Color.parseColor("#BB1a0000") else Color.TRANSPARENT)
                setOnClickListener {
                    currentTab = i
                    renderTab()
                    (parent as? LinearLayout)?.let { bar ->
                        for (j in 0 until bar.childCount) {
                            (bar.getChildAt(j) as? TextView)?.setBackgroundColor(
                                if (j == i) Color.parseColor("#BB1a0000") else Color.TRANSPARENT
                            )
                        }
                    }
                }
            }
            tabBar.addView(tab)
        }
        addView(tabBar)
        renderTab()
    }

    private fun renderTab() {
        contentArea.removeAllViews()
        when (currentTab) {
            TAB_AIMBOT -> buildAimbotTab()
            TAB_ESP    -> buildEspTab()
            TAB_CONFIG -> buildConfigTab()
            TAB_INFO   -> buildInfoTab()
        }
    }

    private fun buildAimbotTab() {
        contentArea.addView(rowOf(
            checkItem("Ativar Aimbot", aimbotAtivo) { v -> aimbotAtivo = v; aimbotEngine.enabled = v },
            checkItem("Puxar em Paredes", puxarParedes) { v -> puxarParedes = v }
        ))
        contentArea.addView(rowOf(
            checkItem("Exibir FOV", exibirFov) { v -> exibirFov = v },
            checkItem("Exibir Linha", exibirLinha) { v -> exibirLinha = v }
        ))
        contentArea.addView(sliderItem("Regular FOV", fovValue, 10f, 360f) { v ->
            fovValue = v; aimbotEngine.fov = v
        })
        conten
