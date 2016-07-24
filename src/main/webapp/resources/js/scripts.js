/**
 * 
 */
function showButton() {
		alert('flksjdf');
// 				$('changeStatus').rendered = true;
}

function handleComplete(xhr, status, args) {
	alert(args.schedule);

	if (args.schedule = true) {
		document.getElementById('delayWorkdays').style.display = 'block';
		document.getElementById('avScheduleWorkdays').style.display = 'none';
	}
	else {
		document.getElementById('delayWorkdays').style.display = 'none';
		document.getElementById('avScheduleWorkdays').style.display = 'block';			}
}
