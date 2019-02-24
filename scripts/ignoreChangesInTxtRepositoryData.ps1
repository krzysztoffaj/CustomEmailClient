$files = Get-ChildItem ./txtRepositoryData/ -Recurse -File|
		Foreach-Object {$_.FullName}

for ($i = 0; $i -lt $files.Count ; $i++) {
	git update-index --assume-unchanged $files[$i]
}
