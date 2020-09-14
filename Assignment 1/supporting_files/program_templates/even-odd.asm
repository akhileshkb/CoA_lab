	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	andi %x3, 1, %x4
	addi %x0, 1, %x5
	beq %x4, %x5, success
	beq %x4, %x0, endl
success:
	addi %x0, 1, %x10
	end
endl:
	subi %x0, 1, %x10
	end