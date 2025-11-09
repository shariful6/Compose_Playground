package com.easylife.composeplayground.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ConstraintLayoutDemo(){

    ConstraintLayout(
        modifier = Modifier.background(Color.LightGray).size(250.dp)
    ) {
        // constrain layout example
        val (title, button) = createRefs()

        Text(
            text = "This is title",
            modifier = Modifier.background(Color.Yellow)
                .constrainAs(title){
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            text = "Click Me",
            modifier = Modifier.background(Color.Cyan).constrainAs(button){
                top.linkTo(title.bottom, margin = 0.dp)
                start.linkTo(title.start)
                end.linkTo(title.end)
                bottom.linkTo(parent.bottom)
            }
        )

        //Guideline example
        val guideline = createGuidelineFromStart(0.25f)// 25% space will be dedicated at start
        Text(
            text = "Aligned at 25%",
            modifier = Modifier.constrainAs(createRef()){
                start.linkTo(guideline)
            }
        )

        //Horizontal chain example
        val (box1, box2, box3) = createRefs()
        Box(modifier = Modifier.size(50.dp).background(Color.Red).constrainAs(box1){top.linkTo(parent.top, 50.dp)})
        Box(modifier = Modifier.size(50.dp).background(Color.Green).constrainAs(box2){top.linkTo(parent.top, 50.dp)})
        Box(modifier = Modifier.size(50.dp).background(Color.Blue).constrainAs(box3){top.linkTo(parent.top, 50.dp)})
        createHorizontalChain(box1, box2, box3, chainStyle = ChainStyle.Spread)

    }
}

@Preview(showBackground = true)
@Composable
fun ConstraintLayoutDemoPreview() {
    ConstraintLayoutDemo()
}