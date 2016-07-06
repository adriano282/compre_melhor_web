/**
 * 
 */
PrimeFaces.validator['BarCode'] = {
		pattern: /\d{13}/,
		MESSAGE_ID: 'com.compremelhor.web.validation.barcode.invalid',
		validate: function(element, value) {
			var vc = PrimeFaces.util.ValidationContext;
			
			if (!this.pattern.test(value) || !checkVerifierDigit()) {
				var msgStr = element.data('cc-msg');
				var msg = msgStr ? {summary:msgStr, detail:msgStr} : vc.getMessage(this.MESSAGE_ID);
				throw msg; 
			}
		}
}

function checkVerifierDigit() {
	var i = 1;
	var sumOddNumbers = 0;
	var resultEvenNumbers = 0;
	
	for (var s in value.split("")) {
		if (i == 13) break;
		
		if (i++ % 2 != 0) {
			sumOddNumbers += Integer.valueOf(s);
		} 		
		else {
			resultEvenNumbers += Integer.valueOf(s) * 3;
		} 
	}
	
	var result = resultEvenNumbers + sumOddNumbers;
	
	var verifier = 0;
	
	while (result++ % 10 != 0) {
		verifier++;
	}
	
	return verifier == value.split("")[12];
}