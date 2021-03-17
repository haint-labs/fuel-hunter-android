package fuel.hunter.scenes.details

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.request.ImageRequest
import fuel.hunter.R
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.max

sealed class UIMarker {
    object Empty : UIMarker()
    data class Ready(val drawable: Drawable) : UIMarker()
}

@Composable
fun DetailsScene() {
    Box(modifier = Modifier.fillMaxSize()) {
        val url = remember { "http://162.243.16.251/fuel_hunter/rn/rn_logo@2x.png" }
        val scope = rememberCoroutineScope()

        val ctx = LocalContext.current
        val (marker, setMarker) = remember { mutableStateOf<UIMarker>(UIMarker.Empty) }

        DisposableEffect(url) {
            val job = scope.launch {
                val request = ImageRequest.Builder(ctx)
                    .data(url)
                    .size(120, 120)
                    .allowHardware(false)
                    .build()

                val result = ImageLoader.Builder(ctx)
                    .build()
                    .execute(request)

                val icon = result.drawable ?: return@launch

                Log.d("MOX", " I got image")

                setMarker(UIMarker.Ready(icon))
            }

            onDispose { job.cancel() }
        }

        AndroidView(
            factory = {
                Configuration
                    .getInstance()
                    .load(it, PreferenceManager.getDefaultSharedPreferences(it))

                MapView(it)
            },
            update = {
                it.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                it.setMultiTouchControls(true)
                with(it.controller) {
                    setZoom(14.0)
                    setCenter(GeoPoint(56.9509, 24.2386))
                }

                if (marker is UIMarker.Ready) {
                    val m = Marker(it)
                    m.position = GeoPoint(56.9509, 24.2386)
                    m.icon = createDrawable(ctx, marker.drawable)

                    it.overlays.add(m)
                }
            }
        )
    }
}

fun createDrawable(ctx: Context, drawable: Drawable): BitmapDrawable {
    val bitmap = android.graphics.Bitmap
        .createBitmap(150, 150, android.graphics.Bitmap.Config.ARGB_8888)

    val canvas = android.graphics.Canvas(bitmap)

    with(canvas) {
        val paint = android.graphics.Paint()
        paint.color = android.graphics.Color.WHITE

        drawRect(20f, 20f, width - 20f, height - 20f, paint)

        drawable.setBounds(30, 30, width - 30, height - 30)
        drawable.draw(this)
    }

    return BitmapDrawable(ctx.resources, bitmap)
}

@Composable
fun FHMarker(
    bitmap: Bitmap,
    contentPadding: Dp = 2.dp,
    backgroundColor: Color = Color.White,
    shadowColor: Color = Color.Red,
    cornerRadius: Dp = 6.dp,
) {
    val padding = with(LocalDensity.current) { contentPadding.toPx() }

    val radius = with(LocalDensity.current) { cornerRadius.toPx() }

    val shadowStroke = with(LocalDensity.current) { 1.dp.toPx() }

    val backgroundPaint by remember {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
            color = backgroundColor.toArgb()
        }

        mutableStateOf(paint)
    }

    val shadowPaint by remember {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            color = shadowColor.toArgb()
            strokeWidth = 0f
            maskFilter = BlurMaskFilter(shadowStroke * 3f, BlurMaskFilter.Blur.OUTER)
        }

        mutableStateOf(paint)
    }

    Canvas(modifier = Modifier.size(70.dp)) {
        drawIntoCanvas {
            val h = size.height
            val w = size.width

            with(it.nativeCanvas) {
                val path = android.graphics.Path().apply {
                    addRoundRect(
                        0f, 0f,
                        w, h - (h * 0.125f),
                        radius, radius,
                        android.graphics.Path.Direction.CCW
                    )

                    moveTo(w / 2, h)
                    lineTo((w / 2) + (w * 0.125f), h - (h * 0.125f))
                    lineTo((w / 2) - (w * 0.125f), h - (h * 0.125f))
                    lineTo(w / 2, h)
                }

                drawPath(path, shadowPaint)
                drawPath(path, backgroundPaint)

                val scale = max(w - padding * 2, h - (h * 0.125f) - padding * 2) / max(bitmap.width, bitmap.height)

                android.graphics.Matrix().apply { scale(scale, scale) }
                drawBitmap(bitmap, 10f, 10f, null)
            }
        }
    }
}


@Preview
@Composable
fun FHMarkerPreview() {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        FHMarker(
            bitmap = ImageBitmap.imageResource(id = R.drawable.logo_neste).asAndroidBitmap()
        )
    }
}