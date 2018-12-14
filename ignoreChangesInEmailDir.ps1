$email = Get-ChildItem ./emails/ -Recurse -File|
		Foreach-Object {$_.FullName}

for ($i = 0; $i -lt $email.Count ; $i++) {
	git update-index --assume-unchanged $email[$i]
}