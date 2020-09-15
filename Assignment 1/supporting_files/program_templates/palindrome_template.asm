	.data
a:
	101
	.text
main:
	load %x0, $a, %x3
	add %x3, %x0, %x5
	addi %x0, 0, %x4
loop: 
	divi %x3, 10, %x3
	muli %x4, 10, %x4
	add %x31, %x4, %x4
	beq %x4, %x5, success
	beq %x3, %x0, endl
	jmp loop
success:
	addi %x0, 1, %x10
	end
endl:
	subi %x0, 1, %x10
	end
