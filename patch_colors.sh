sed -i 's/Color(0xFF059669)/ColorBehaviorApproach/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFFDC2626)/ColorBehaviorAvoid/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFFD97706)/ColorBehaviorFeed/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFF2563EB)/ColorBehaviorOrient/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFF0D9488)/ColorBehaviorExplore/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFF64748B)/ColorBehaviorRest/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFF7C3AED)/ColorBehaviorGroom/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt

sed -i 's/Color(0xFF10B981)/ColorModulatorOctopamine/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFF3B82F6)/ColorModulatorDopamine/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
sed -i 's/Color(0xFF8B5CF6)/ColorModulatorSerotonin/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt

sed -i 's/import com.flylab.sim.SimulationSnapshot/import com.flylab.sim.SimulationSnapshot\nimport com.flylab.ui.theme.*/' app/src/main/java/com/flylab/ui/components/SensoryMotorDashboard.kt
