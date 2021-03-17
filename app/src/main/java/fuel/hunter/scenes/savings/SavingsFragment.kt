package fuel.hunter.scenes.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fuel.hunter.R
import fuel.hunter.databinding.FragmentComposeBinding
import fuel.hunter.scenes.base.BaseLayout
import fuel.hunter.scenes.base.GlowingToolbar
import fuel.hunter.scenes.base.rememberToolbarState

class SavingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentComposeBinding.inflate(inflater, container, false)
        val navController = findNavController()

        view.composeView.setContent {
            SavingsScene(navController::navigateUp)
        }

        return view.root
    }
}

private val items = listOf(
    R.drawable.ic_bag to R.string.savings_bag_description,
    R.drawable.ic_map to R.string.savings_map_description,
    R.drawable.ic_bell to R.string.savings_bell_description,
    R.drawable.ic_bell to R.string.savings_bell_description,
    R.drawable.ic_star to R.string.savings_star_description,
)

@Preview
@Composable
fun SavingsScene(
    onNavigationClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState(0)
    val toolbarState = rememberToolbarState(
        color = colorResource(id = R.color.colorPrimary),
        maxAlpha = with(LocalDensity.current) { 50.dp.toPx() }
    )

    toolbarState.alpha = scrollState.value.toFloat()

    BaseLayout(
        toolbar = {
            GlowingToolbar(
                text = stringResource(id = R.string.title_savings),
                navigationIcon = {
                    Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_arrow), null)
                },
                toolbarState = toolbarState,
                onNavigationClick = onNavigationClick,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(horizontal = 17.dp)
        ) {
            items.forEachIndexed { index, (iconId, descriptionId) ->
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = iconId),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 17.dp)
                        )

                        Text(
                            text = stringResource(id = descriptionId),
                            color = colorResource(id = R.color.colorPrimary),
                            fontSize = 16.sp,
                        )
                    }

                    if (index < items.lastIndex) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorResource(id = R.color.colorPrimary))
            )

            Text(
                text = stringResource(id = R.string.savings_example),
                color = colorResource(id = R.color.colorPrimary),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        }
    }
}
