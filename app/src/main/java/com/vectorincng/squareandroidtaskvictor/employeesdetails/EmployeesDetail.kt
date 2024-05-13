package com.vectorincng.squareandroidtaskvictor.employeesdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmployeesDetailsScreen() {

}


@Composable
fun EmployeesDetailsListItem() {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        /*Image(
            modifier = Modifier.size(50.dp),
            //painter = painterResource(id = item.image),
        )*/
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            //Text(text = item.title, style = MaterialTheme.typography.headlineMedium)
            //Text(text = item.subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun EmployeesDetailsContent() {

}

